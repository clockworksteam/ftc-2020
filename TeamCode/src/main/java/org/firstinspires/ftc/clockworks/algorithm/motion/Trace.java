package org.firstinspires.ftc.clockworks.algorithm.motion;

public class Trace {
    private final Point[] points;
    private final double roughTolerance;
    private final double fineTolerance;
    private final boolean fullStop;

    public Trace(Point[] points, double roughTolerance, double fineTolerance, boolean fullStop) {
        this.points = points;
        this.roughTolerance = roughTolerance;
        this.fineTolerance = fineTolerance;
        this.fullStop = fullStop;
    }

    public Point[] getPoints() {
        return points;
    }

    public double getRoughTolerance() {
        return roughTolerance;
    }

    public double getFineTolerance() {
        return fineTolerance;
    }

    public boolean hasFullstop() {
        return fullStop;
    }

    public boolean hasPoint(Point p) {
        for (Point pt : points) {
            if ( p == pt) return true;
        }
        return false;
    }
}
