package org.firstinspires.ftc.clockworks.algorithm.odometry;

//"TriOdometry" interface. Every class for odometry should implement it.
public interface TriOdometry {
	double getX();
	double getY();
	double getOrientation();
	double feed(int left, int right, int mid);
}
