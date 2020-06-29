package org.firstinspires.ftc.clockworks.algorithm.motion;

import com.vuforia.Trackable;

public class Trace {

    @FunctionalInterface
    public interface Listener {
        void pos(double x, double y);
    }

    private final Point[] points;
    private Listener listener;

    public Trace(Point[] points) {
        this.points = points;
    }

    public Trace setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    void callListener(double x, double y) {
        if (listener != null) listener.pos(x, y);
    }

    public Point[] getPoints() {
        return points;
    }

    public boolean hasPoint(Point p) {
        for (Point pt : points) {
            if ( p == pt) return true;
        }
        return false;
    }
}
