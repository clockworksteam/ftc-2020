package org.firstinspires.ftc.clockworks.helpers;

/**
 * This class is used to determine the optimal angle for the robot to rotate at
 * It consists of two methods, norm and optimal distance
 */
public class AngleHelper {

	/**
	 * norm method description:
	 * @param angle it takes as a parameter an angle
	 * @return it returns the angle in the [-180;180] range
	 */
	public static double norm(double angle) {
		angle = angle % 360;
		if (angle > 180) angle -= 360;
		if (angle < -180) angle += 360;
		return angle;
	}

	/**
	 * optimalDistance description:
	 * @param to is the target angle
	 * @param from is the starting angle
	 * @return it returns the optimal distance
	 */
	public static double optimalDistance(double to, double from) {
		double dist = to - from;
		while (dist > 180) dist = dist - 360;
		while (dist < -180) dist = dist + 360;
		return dist;
	}
}
