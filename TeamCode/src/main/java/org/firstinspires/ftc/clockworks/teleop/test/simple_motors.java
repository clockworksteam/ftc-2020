package org.firstinspires.ftc.clockworks.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class simple_motors extends LinearOpMode {
    private DcMotor left_motor = null;
    private DcMotor right_motor = null;

    @Override
    public void runOpMode() throws InterruptedException {
        left_motor = hardwareMap.get(DcMotor.class, "left");
        right_motor = hardwareMap.get(DcMotor.class, "right");

        left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive()){
            left_motor.setPower(gamepad1.left_stick_y);
            right_motor.setPower(gamepad1.right_stick_y);

            Thread.sleep(100);
        }
    }
}
