package org.firstinspires.ftc.clockworks.algorithm.motion;

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
        NAVIGATING, STOPPING,
    }

    private final BlockingQueue<Trace> queue = new ArrayBlockingQueue<Trace>(QUEUE_SIZE);

    private InternalScheduler scheduler;


    private final PositionController movementExecutor;
    private final Odometry odometry;

    private MotionStrategy strategy = null;
    private HashMap<String, MotionStrategy> strategyMap = new HashMap<>();

    private State state = State.STOPPING;
    private Point lastTarget = null;
    private Trace activeTrace = null;
    private Point activeTarget = null;
    private double range = 0;

    public MotionCommander(PositionController movementExecutor, Odometry odometry) {
        this.movementExecutor = movementExecutor;
        this.odometry = odometry;
        strategyMap.put("stopping", new StoppingStrategy());
        strategyMap.put("navigating", new NavigatingStrategy());
    }

    @Override
    public void init(InternalScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void tick() {
        Point lastKnownLocation = queryOdometryPosition();

        if (activeTarget == null) {
            activeTarget = lastKnownLocation;
            lastTarget = lastKnownLocation;
            activeTrace = new Trace(new Point[]{ activeTarget }, 0, 0);
        }

        if ((state == State.NAVIGATING && range * range >= Point.distanceSquared(activeTarget, lastKnownLocation)) || state == State.STOPPING) {
            Point next = tryGetNextTarget();
            if (next == null) {
                state = State.STOPPING;
                renewStrategy(null);
            } else {
                activeTrace = activeTrace.hasPoint(next) ? activeTrace : queue.poll();
                range = next.isRough() ? activeTrace.getSkipTolerance() : activeTrace.getContinueTolerance();
                state = State.NAVIGATING;
                renewStrategy(next);
            }
        }

        if (strategy != null) strategy.run(lastKnownLocation, movementExecutor);

        movementExecutor.update();
    }

    @Override
    public void deinit() {
        // Dummy method
    }

    private void renewStrategy(Point next) {
        if (next != null) {
            lastTarget = activeTarget;
            activeTarget = next;
        }
        strategy = null;
        if (state == State.NAVIGATING) {
            strategy = strategyMap.get("navigating");
        }
        if (state == State.STOPPING) {
            strategy = strategyMap.get("stopping");
        }
        if (strategy != null) {
            strategy.begin(lastTarget, activeTarget);
        }
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
