package org.firstinspires.ftc.clockworks.hardware.test;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MotorsForTesting {
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
     * @param hardwareMap the hardware map. Must contain baseHardwareName + "_left_back", baseHardwareName + "_left_front", baseHardwareName + "_right_back",  and baseHardwareName + "_right_front"
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
    }}
