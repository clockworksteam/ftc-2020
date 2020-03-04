package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.clockworks.hardware.ServoGripper;

@TeleOp
public class ServoGripperOP extends LinearOpMode {
    ServoGripper servoGripper = null;


    @Override
    public void runOpMode() throws InterruptedException {
        servoGripper = new ServoGripper();

        servoGripper.init(hardwareMap , telemetry);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x)
                servoGripper.close_gripper();

            if (gamepad1.a)
                servoGripper.open_gripper();

        }
    }
}
