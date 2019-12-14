package org.firstinspires.ftc.clockworks.algorithm;

import org.firstinspires.ftc.clockworks.helpers.AngleHelper;

/**
 * This class is an implementation of PID for rotation
 * It uses the same variables as the PID algorithm and overrides the feed method in order to be used for rotating
 */
public class AngularPID extends PID {

	/**
	 * Initialization
	 * @param kp constant for the proportional
	 * @param ki constant for the integral
	 * @param kd constant for the derivative
	 */
	public AngularPID(double kp, double ki, double kd) {
		super(kp, ki, kd);
	}

	/**
	 * The derivative measures the error in the elapsed time
	 * The integral approximates the area of error (how far the robot has deviated)
	 * @param value input value
	 * @param time the elapsed time
	 * @return returning the correction
	 */
	@Override
	public double feed(double value, double time) {
		double error = AngleHelper.optimalDistance(target, value);
		double derivative = AngleHelper.optimalDistance(error, lastError) / (time - lastTime);
		integral += (time * error);
		lastError = error;
		lastTime = time;
		return error * kp + integral * ki + derivative * kd;
	}
}
