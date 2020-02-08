package org.firstinspires.ftc.clockworks.algorithm.motion;

import org.firstinspires.ftc.clockworks.helpers.WaitableInteger;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MotionCommander extends Thread {
    private final BlockingQueue<Trace> queue = new ArrayBlockingQueue<Trace>(30);

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

    private void drive(Trace trace, Point from, Point to) {
        Point location  = queryOdometry();
        double range = to.isRough() ? trace.getRoughTolerance() : trace.getFineTolerance();
        while (range * range < Point.distanceSquared(location, to)) {
            // Drive tnagent to the line. PID perpendicular to the line.
        }
        if (!to.isRough()) {
            // PID X,Y towards point
        } else {
            // Set final oriesntation
        }
    }

    private Trace getOneTrace() {
        Trace trace = null;
        boolean success = false;
        while (!success) {
            try {
                trace = queue.take();
                success = true;
            } catch (InterruptedException iex) { }
        }
        return trace;
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