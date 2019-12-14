package org.firstinspires.ftc.clockworks.hardware.test;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODERS;

/**
 * A class used to control the gripper and the paralelogram of the robot
 * It presents two methods: grip and paralel, which control the two parts of the mechanism
 */
public class Gripper {
    private Servo servo = null;
    private DcMotor motor = null;
    private HardwareMap hardwareMap = null;
    private int ser = 0;
    private int E = 0;

    public  void init(HardwareMap hardwareMap, String basename) {
        servo = hardwareMap.get(Servo.class, basename+"_servo");
        motor = hardwareMap.get(DcMotor.class, basename+"_motor");

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotor.Direction.FORWARD);
        servo.setDirection(Servo.Direction.FORWARD);
    }

    /**
     * The grip method moves the servo of the gripper to the closed position in case it's open and open in case it's closed
     */
    public void grip() {
        if(ser == 0) {
            servo.setPosition(0.5);
            ser++;
        }else {
            servo.setPosition(0);
            ser--;
        }
    }

    /**
     * The paralel method moves the paralelogram up and down depending on it's current position
     */
    public void paralel() {
        if(E == 0) {

            motor.setTargetPosition(300);
            motor.setPower(0.05);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            E = 666;
        }else {

            motor.setTargetPosition(0);
            motor.setPower(0.05);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            E = 0;
        }
    }

}
