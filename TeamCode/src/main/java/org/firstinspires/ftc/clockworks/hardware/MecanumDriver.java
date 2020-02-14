package org.firstinspires.ftc.clockworks.hardware;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Driver class for movement with motors in MecanumDriver configuration.
 * Requires both left and right motors to exist in the hardware map.
 */
public class MecanumDriver {
	private Telemetry telemetry = null;
	private DcMotor left_back = null;
	private DcMotor right_back = null;
	private DcMotor left_front = null;
	private DcMotor right_front = null;
	private double maxSpeed = 1;
	private double currentDirection = 0;
	private double currentSpeed = 0;
	private double angular_speed;

	/**
  -	 * Initializes the driver
	 * @param telemetry the telemetry logger
	 * @param hardwareMap the hardware map configured in the app for Expansion Hub. Must contain baseHardwareName + "_left_back", baseHardwareName + "_left_front", baseHardwareName + "_right_back",  and baseHardwareName + "_right_front"
	 * @param baseHardwareName the name of the device
	 * @throws IllegalArgumentException when the hardware map does not contain all baseHardwareName + "_left_back", baseHardwareName + "_left_front", baseHardwareName + "_right_back",  and baseHardwareName + "_right_front"
	 */
	public void init(Telemetry telemetry, HardwareMap hardwareMap, String baseHardwareName) {
		this.telemetry = telemetry;
		left_back = hardwareMap.get(DcMotor.class, baseHardwareName + "_left_back");
		right_back = hardwareMap.get(DcMotor.class, baseHardwareName + "_right_back");
		left_front = hardwareMap.get(DcMotor.class, baseHardwareName + "_left_front");
		right_front = hardwareMap.get(DcMotor.class, baseHardwareName + "_right_front");

		if (left_back == null || left_front == null || right_back == null || right_front == null) {
			throw new IllegalArgumentException("the hardware map does not contain all baseHardwareName + \"_left_back\", baseHardwareName + \"_left_front\", baseHardwareName + \"_right_back\",  and baseHardwareName + \"_right_front\"");
		}

		left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		left_back.setDirection(DcMotorSimple.Direction.FORWARD);
		right_back.setDirection(DcMotorSimple.Direction.REVERSE);
		left_front.setDirection(DcMotorSimple.Direction.FORWARD);
		right_front.setDirection(DcMotorSimple.Direction.REVERSE);


		left_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		right_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		left_back.setPower(0);
		right_back.setPower(0);
		left_front.setPower(0);
		right_front.setPower(0);

		telemetry.addLine("Initialized motors succesfully");
	}

	/**
	 * Limits the motors to a maximum speed. The motors are not cut off
	 * at the new maximum speed, but instead are scaled to the new max
	 * @param speed the new maximum speed. must be in range [0, 1000]
	 * @return self reference (this). Useful in chaining methods
	 */
	public MecanumDriver setMaximumSpeed(double speed) {
		maxSpeed = Range.clip(speed, 0, 1000);
		maxSpeed /= 1000;
		updateMotors();
		return this;
	}

	/**
	 * Gets the maximum speed
	 * @return the maximum speed
	 */
	public double getMaximumSpeed() {
		return maxSpeed * 1000;
	}

	/**
	 * Drive the motors
	 * @param speed The speed to move at. Must be in range [0, 1000] Affected by maximum speed
	 * @param direction The direction, in angles, relative to current position.
	 *                  0 moves forward
	 *                  (+) positive angles steer left
	 *                  (-) negative angles steer right
	 * @return self reference (this). Useful in chaining methods
	 */
	public MecanumDriver drive(double speed, double direction, double angular_speed) {
		currentDirection = direction;
		currentSpeed = Range.clip(speed, 0, 1);
		this.angular_speed = angular_speed;
		updateMotors();
		return this;
	}


	/**
	 * Drive the motors
	 * direction=the current direction ,degrees->radians
	 * speed for moving
	 */
	private void updateMotors() {

		double v[] = new double[4];
		double r[] = new double[4];

		double direction = (currentDirection + 45) * Math.PI / 180;

		v[0] = (Math.cos(direction) * maxSpeed * currentSpeed);   //
		v[1] = (Math.sin(direction) * maxSpeed * currentSpeed);   //
		v[2] = -(-Math.sin(direction) * maxSpeed * currentSpeed); //
		v[3] = -(-Math.cos(direction) * maxSpeed * currentSpeed); //

		r[0] = -angular_speed;
		r[1] = angular_speed;
		r[2] = -angular_speed;
		r[3] = angular_speed;

		for (int k =0; k<4; k++) v[k] += r[k];

		double maxim = Math.max(Math.max((Math.abs(v[0])) , (Math.abs(v[1])))
				, Math.max ((Math.abs(v[2])) , (Math.abs(v[3]))));

		for (int k=0; k<4 && maxim > 1; k++) v[k] /= maxim;

		left_front.setPower(v[0]);
		right_front.setPower(v[1]);
		left_back.setPower(v[2]);
		right_back.setPower(v[3]);
	}
}
