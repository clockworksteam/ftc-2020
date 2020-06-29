package org.firstinspires.ftc.clockworks.algorithm.odometry;

public interface TriOdometry {
	double getX();
	double getY();
	double getOrientation();
	double feed(int left, int right, int mid);
}
