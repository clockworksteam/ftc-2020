package org.firstinspires.ftc.clockworks.algorithm.motion;

public class Point {
    private final double x, y, rot;
    private final boolean rough;

    public Point(double x, double y, double rot, boolean rough) {
        this.x = x;
        this.y = y;
        this.rot = rot;
        this.rough = rough;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRot() {
        return rot;
    }

    public boolean isRough() {
        return rough;
    }

    public static double distanceSquared(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }
}