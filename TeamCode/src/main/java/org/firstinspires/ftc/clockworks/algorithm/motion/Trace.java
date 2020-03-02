package org.firstinspires.ftc.clockworks.algorithm.motion;

public class Trace {
    private final Point[] points;
    private final double skipTolerance;
    private final double continueTolerance;

    public Trace(Point[] points, double skipTolerance, double continueTolerance) {
        this.points = points;
        this.skipTolerance = skipTolerance;
        this.continueTolerance = continueTolerance;
    }

    public Point[] getPoints() {
        return points;
    }

    public double getSkipTolerance() {
        return skipTolerance;
    }

    public double getContinueTolerance() {
        return continueTolerance;
    }

    public boolean hasPoint(Point p) {
        for (Point pt : points) {
            if ( p == pt) return true;
        }
        return false;
    }
}
