package org.firstinspires.ftc.clockworks.algorithm.motion;

public class Point {
    private final double x, y, tolerance;

    public Point(double x, double y, double tolerance) {
        this.x = x;
        this.y = y;
        this.tolerance = tolerance;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTolerance() {
        return tolerance;
    }

    public static double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }
}
