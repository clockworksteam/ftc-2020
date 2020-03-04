package org.firstinspires.ftc.clockworks.algorithm.motion;

public class Point {
    private final double x, y;
    private final boolean rough;

    public Point(double x, double y, boolean rough) {
        this.x = x;
        this.y = y;
        this.rough = rough;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isRough() {
        return rough;
    }

    public static double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }
}
