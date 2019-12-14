package org.firstinspires.ftc.clockworks.hardware.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * This class defines two motors m1 and m2 and declares two methods moveLeft and moveRight in order to test mechanisms using Dc motors
 */

public class Dctester {
    //Motor declaration
    private DcMotor m1 = null;
    private DcMotor m2 = null;

    /**
     * description of init method:
     *
     * @param hardwareMap      is used to set motor's names in the configuration
     * @param baseHardwareName is the root name for motors
     */
    public void init(HardwareMap hardwareMap, String baseHardwareName) {
        m1 = hardwareMap.get(DcMotor.class, baseHardwareName + "right");
        m2 = hardwareMap.get(DcMotor.class, baseHardwareName + "left");

        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        m1.setDirection(DcMotorSimple.Direction.FORWARD);
        m2.setDirection(DcMotorSimple.Direction.REVERSE);


        m1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        m1.setPower(0);
        m2.setPower(0);
    }


    /**
     * description of moveLeft method:
     *
     * @param left is the power given to the left motor
     */
    public void moveLeft(float left) {
        m2.setPower(left);
    }

    /**
     * description of moveRight method:
     *
     * @param right is the power given to the right motor
     */
    public void moveRight(float right) {
        m1.setPower(right);
    }

    /**
     * This class is used to control two servo motors in order to test mechanisms involving this type of motors
     * There are two methods in this class used to control the position of their respective motor
     */
}