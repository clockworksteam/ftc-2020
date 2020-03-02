package org.firstinspires.ftc.clockworks.algorithm.motion;

import org.firstinspires.ftc.clockworks.algorithm.PID;
import org.firstinspires.ftc.clockworks.algorithm.motion.strategy.MotionStrategy;
import org.firstinspires.ftc.clockworks.algorithm.motion.strategy.NavigatingStrategy;
import org.firstinspires.ftc.clockworks.algorithm.motion.strategy.StoppingStrategy;
import org.firstinspires.ftc.clockworks.control.Odometry;
import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MotionCommander implements Fiber {
    private static final int QUEUE_SIZE = 30;


    private enum State {
        NAVIGATING, CENTERING, STOPPED,
    }

    private final BlockingQueue<Trace> queue = new ArrayBlockingQueue<Trace>(QUEUE_SIZE);

    private InternalScheduler scheduler;


    private final PositionController movementExecutor;
    private final Odometry odometry;

    private MotionStrategy strategy = null;
    private HashMap<MotionStrategy, String> strategyMap = new HashMap<>();

    private State state = State.CENTERING;
    private Point lastKnownLocation = null;
    private Point lastTarget = null;
    private Trace activeTrace = null;
    private Point activeTarget = null;
    private double range = 0;

    public MotionCommander(PositionController movementExecutor, Odometry odometry) {
        this.movementExecutor = movementExecutor;
        this.odometry = odometry;
        strategyMap.put(new StoppingStrategy(), "stopping");
        strategyMap.put(new NavigatingStrategy(), "navigating");
    }

    @Override
    public void init(InternalScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void tick() {
        lastKnownLocation = queryOdometryPosition();

        if (activeTarget == null) {
            activeTarget = lastKnownLocation;
            lastTarget = null;
            activeTrace = new Trace(new Point[]{ activeTarget }, 0, 0);
        }

        if (state == State.NAVIGATING && range * range > Point.distanceSquared(activeTarget, lastKnownLocation)) {
            if (activeTarget.isRough()) {
                Point next = tryGetNextTarget();
                if (next == null) {

                } else {
                    activeTrace = activeTrace.hasPoint(next) ? activeTrace : queue.poll();
                    range = next.isRough() ? activeTrace.getSkipTolerance() : activeTrace.getContinueTolerance();

                }
            } else {
                range = activeTrace.getContinueTolerance();
            }
        }


        if (strategy != null) strategy.run(lastKnownLocation, movementExecutor);

        movementExecutor.update();
    }

    @Override
    public void deinit() {
        // Dummy method
    }


    private Point tryGetNextTarget() {
        Point next = null;
        Point[] points = activeTrace.getPoints();
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == activeTarget) next = points[i + 1];
        }
        if (next == null && !queue.isEmpty()) {
            next = queue.peek().getPoints()[0];
        }
        return next;
    }

    private Point queryOdometryPosition() {
        return new Point(odometry.getX(), odometry.getY(), odometry.getOrientation() ,false);
    }

    public Trace registerTrace(Trace trace) {
        if (trace != null) {
            boolean result = queue.offer(trace);
            if (!result) throw new IllegalStateException("The concurrent queue has reached its limit. Increase its maximum limit.");
        }
        return trace;
    }

    public void exit() {
        scheduler.unregister(this);
    }


}
