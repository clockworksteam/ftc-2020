package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.clockworks.algorithm.AngularPID;
import org.firstinspires.ftc.clockworks.algorithm.PID;

//de testat
public class Gripping {
	private Servo upperservo = null;
	private Servo pushservo = null;
	private DcMotor motor = null;
	private DcMotor rotmotor = null;
	private boolean lower_mode = false;
	private int rotm = 0;
	private double power;
	private double apower;
	private boolean lifting = false;

	private PID pid = new PID(0.02, 0, 0);
	private AngularPID angularpid = new AngularPID(0.02, 0, 0);


	public void init(HardwareMap hardwareMap, String basename) {
		motor = hardwareMap.get(DcMotor.class, basename + "_motor");
		rotmotor = hardwareMap.get(DcMotor.class, basename + "_rotparalelmotor");

		motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rotmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		motor.setDirection(DcMotor.Direction.FORWARD);
		rotmotor.setDirection(DcMotor.Direction.FORWARD);

		motor.setTargetPosition(motor.getCurrentPosition());
		rotmotor.setTargetPosition(rotmotor.getCurrentPosition());

        motor.setPower(0.2);
        rotmotor.setPower(0.2);

        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rotmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		upperservo = hardwareMap.get(Servo.class, basename + "_servo");
		pushservo = hardwareMap.get(Servo.class, basename + "_lowerservo");

		upperservo.setDirection(Servo.Direction.FORWARD);
		pushservo.setDirection(Servo.Direction.FORWARD);

		upperservo.setPosition(0);
		pushservo.setPosition(0);
	}

	public void openFront() {
		upperservo.setPosition(0.4);
	}

	public void closeFront() {
		upperservo.setPosition(0);
	}

	public void openBack() {
		pushservo.setPosition(0.4);
	}

	public void closeBack() {
		pushservo.setPosition(0);
	}

	/**
	 * Moves the paralelogram up and down depending on it's current position
	 */
	public void paralelup() {

        motor.setTargetPosition(-500);

	}

	public void paraleldown() {

	    if(lower_mode) {
	        motor.setTargetPosition(10);
        }else {
            motor.setTargetPosition(-10);
        }

        motor.setPower(0.2);

		// TODO: Refuse to lower the paralelogram beyond a safe limit while it is over the robot! This may cause the motor to force itself and overheat at big speeds!
	}

	public void liftUp() {
		lifting = true;
		motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		motor.setPower(-0.3);
	}

	public void liftDown() {
		lifting = true;
		motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		motor.setPower(0.2);
	}

	public void liftLock() {
		if (lifting) {
			motor.setTargetPosition(motor.getCurrentPosition());
			motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			motor.setTargetPosition(motor.getCurrentPosition());
			motor.setPower(0.5);
			lifting = false;
		}
	}

	public void paralelrotationback() {


		// TODO: use motor.setTargetPosition instead of manual PID: a tuned PID controller is already implemented in DcMotor.RunMode.RUN_TO_POSITION
		// use motor.setPower to set the maximum travel speed
        paralelup();
        try {
            Thread.sleep(1500);
        } catch (Exception e) { }
        rotmotor.setTargetPosition(0);
        rotmotor.setPower(0.1);
        try {
            Thread.sleep(1000);
        } catch (Exception e) { }
        lower_mode = false;
        paraleldown();

    }




	public void paralelrotationfront() {


		// TODO: use motor.setTargetPosition instead of manual PID: a tuned PID controller is already implemented in DcMotor.RunMode.RUN_TO_POSITION
		// use motor.setPower to set the maximum travel speed
        paralelup();
        try {
            Thread.sleep(1500);
        }catch (Exception e) { }
		rotmotor.setTargetPosition(-720);
        try {
            Thread.sleep(1500);
        }catch (Exception e) { }
        lower_mode = true;
		paraleldown();

	}


}