package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Driver class for implementing methods used by the IMU(gyro) sensor.
 */
public class IMUSensor {

	public interface AxisCorrection {
		Orientation corrector(Orientation o);
		Acceleration corrector(Acceleration a);
	}

	private Telemetry telemetry = null;
	private BNO055IMU imu = null;
	private AxisCorrection correction = null;
	private Orientation orientation = null;

	/**
	 * Initializes the IMU sensor
	 * @param telemetry the telemetry logger
	 * @param hwMap the hardware map. must contain hardwareName
	 * @param hardwareName the name of the IMU in the hardware map
	 */
	public void init(Telemetry telemetry, HardwareMap hwMap, String hardwareName) {
		init(telemetry, hwMap, hardwareName, null);
	}

	/**
	 * Initializes the IMU sensor
	 * @param telemetry the telemetry logger
	 * @param hwMap the hardware map. must contain hardwareName
	 * @param hardwareName the name of the IMU in the hardware map
	 * @param correction the correction method. A value of null defaults to no correction
	 */
	public void init(Telemetry telemetry, HardwareMap hwMap, String hardwareName, AxisCorrection correction) {
		this.telemetry = telemetry;
		BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
		parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.loggingEnabled = false;
		parameters.loggingTag = "IMU";
		parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
		imu = hwMap.get(BNO055IMU.class, hardwareName);
		imu.initialize(parameters);
		if (correction == null) correction = new IdentityAxisCorrection();
		this.correction = correction;
	}

	/**
	 * Fetches the data from the sensor to local storage
	 */
	public void readDevice() {
		orientation = correction.corrector(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
	}

	/**
	 * Reads the local storage and returns the orientation heading
	 * @return the heading, expressed in angles
	 */
	public double getHeading() {
		return orientation.firstAngle;
	}

	/**
	 * Reads the local storage and returns the orientation elevation
	 * @return the elevation, expressed in angles
	 */
	public double getElevation() {
		return orientation.secondAngle;
	}

	/**
	 * Reads the local storage and returns the orientation bank
	 * @return the bank, expressed in angles
	 */
	public double getBank() {
		return orientation.thirdAngle;
	}

	/**
	 * Reads the IMU and returns the acceleration
	 * @return the acceleration, expressed in meters per second squared
	 */
	public Acceleration getAcceleration() {
		return correction.corrector(imu.getAcceleration());
	}
}

class IdentityAxisCorrection implements IMUSensor.AxisCorrection {
	@Override public Orientation corrector(Orientation o) { return o; }
	@Override public Acceleration corrector(Acceleration a) { return a;	}
}
