package org.firstinspires.ftc.clockworks.algorithm.motion;

import org.firstinspires.ftc.clockworks.algorithm.PID;
import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MotionCommander implements Fiber {
    private static final double NAVIGATION_Kp = 0;
    private static final double NAVIGATION_Ki = 0;
    private static final double NAVIGATION_Kd = 0;
    private static final double CENTERING_Kp = 0;
    private static final double CENTERING_Ki = 0;
    private static final double CENTERING_Kd = 0;
    private static final int QUEUE_SIZE = 30;


    private enum State {
        NAVIGATING, CENTERING, STOPPED,
    }

    private final BlockingQueue<Trace> queue = new ArrayBlockingQueue<Trace>(QUEUE_SIZE);

    private InternalScheduler scheduler;

    private PID navigationCompensator = new PID(NAVIGATION_Kp, NAVIGATION_Ki, NAVIGATION_Kd);
    private PID centeringCompensator = new PID(CENTERING_Kp, CENTERING_Ki, CENTERING_Kd);

    private State state = State.CENTERING;
    private Point lastKnownLocation = null;
    private Point fullStopPosition = null;
    private Trace activeTrace = null;
    private Point activeTarget = null;
    private double range = 0;
    private boolean doneOscillating = false;


    @Override
    public void init(InternalScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void tick() {
        lastKnownLocation = queryOdometryPosition();

        if (activeTarget == null) {
            activeTarget = lastKnownLocation;
            activeTrace = new Trace(new Point[]{ activeTarget }, 0, 0, true);
        }

        if (state == State.NAVIGATING && range * range > Point.distanceSquared(activeTarget, lastKnownLocation)) {
            if (activeTarget.isRough()) {
                Point next = tryGetNextTarget();
                if (next == null) {
                    if (activeTrace.hasFullstop()) {
                        fullStopPosition = lastKnownLocation;
                        state = State.STOPPED;
                        centeringCompensator.reset(System.currentTimeMillis() / 1000.0);
                    } else {
                        range = activeTrace.getRoughTolerance();
                        doneOscillating = false;
                        state = State.CENTERING;
                        centeringCompensator.reset(System.currentTimeMillis() / 1000.0);
                    }
                } else {
                    activeTrace = activeTrace.hasPoint(next) ? activeTrace : queue.poll();
                    activeTarget = next;
                    range = next.isRough() ? activeTrace.getRoughTolerance() : activeTrace.getFineTolerance();
                    navigationCompensator.reset(System.currentTimeMillis() / 1000.0);
                }
            } else {
                range = activeTrace.getFineTolerance();
                doneOscillating = false;
                state = State.CENTERING;
                centeringCompensator.reset(System.currentTimeMillis() / 1000.0);
            }
        }
        if (state == State.CENTERING && doneOscillating) {
            if (activeTrace.hasFullstop()) {
                state = State.STOPPED;
                fullStopPosition = lastKnownLocation;
                centeringCompensator.reset(System.currentTimeMillis() / 1000.0);
            }
        }

        if (state == State.NAVIGATING) {
            navigate();
        }
        if (state == State.CENTERING) {
            center();
        }
        if (state == State.STOPPED) {
            stop();
        }
    }

    @Override
    public void deinit() {
        // Dummy method
    }

    private void navigate() {
        // TODO: Navigation
    }

    private void center() {
        // TODO: Centering
    }

    private void stop() {
        double distance = Point.distanceSquared(lastKnownLocation, fullStopPosition);
        double correction = centeringCompensator.feed(distance, System.currentTimeMillis() / 1000.0);
        // TODO: apply correction
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
        // TODO: Something smart, dunno how.
        return new Point(0, 0, 0 ,false);
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
