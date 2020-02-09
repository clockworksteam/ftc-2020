package org.firstinspires.ftc.clockworks.algorithm.motion;

import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MotionCommander implements Fiber {
    private final BlockingQueue<Trace> queue = new ArrayBlockingQueue<Trace>(30);
    private InternalScheduler scheduler;

    private Point lastKnownLocation = null;
    private Trace activeTrace = null;
    private Point activeTarget = null;

    /*
    @Override
    public void run() {
        Trace trace;
        Point lastPoint = queryOdometry();
        while ((trace = getOneTrace()) != null) {
            for (Point point : trace.getPoints()) {
                drive(trace, lastPoint, point);
                lastPoint = point;
            }
        }
    }
     */

    @Override
    public void init(InternalScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void tick() {
        lastKnownLocation = queryOdometry();

        if (activeTarget == null) {
            activeTarget = lastKnownLocation;
            activeTrace = new Trace(new Point[]{ activeTarget }, 0, 0);
        }

        // todo: driving, hovering, staying
    }

    @Override
    public void deinit() {

    }

    private void drive(Trace trace, Point from, Point to) {
        Point location  = queryOdometry();
        double range = to.isRough() ? trace.getRoughTolerance() : trace.getFineTolerance();
        while (range * range < Point.distanceSquared(location, to)) {
            // Drive tnagent to the line. PID perpendicular to the line.
        }
        if (!to.isRough()) {
            // PID X,Y towards point
        } else {
            // Set final orientation
        }
    }

    private Point queryOdometry() {
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
        boolean result = queue.offer(null);
        if (!result) throw new IllegalStateException("The concurrent queue has reached its limit. Increase its maximum limit.");
    }


}
