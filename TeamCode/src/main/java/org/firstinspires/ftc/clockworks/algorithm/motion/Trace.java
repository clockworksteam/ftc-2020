package org.firstinspires.ftc.clockworks.algorithm.motion;

public class Trace {
    private final Point[] points;
    private final double roughTolerance;
    private final double fineTolerance;

    public Trace(Point[] points, double roughTolerance, double fineTolerance) {
        this.points = points;
        this.roughTolerance = roughTolerance;
        this.fineTolerance = fineTolerance;
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
}
