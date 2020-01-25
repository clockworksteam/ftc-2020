package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.clockworks.hardware.GamepadInput;

@TeleOp(name = "Testmotor", group = "Drive")
public class Testmotor extends LinearOpMode {
    private DcMotor x;
    private GamepadInput controller = new GamepadInput();




    @Override
    public void runOpMode() {
        x = hardwareMap.get(DcMotor.class, "x");
        x.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        controller.init(gamepad1);


        while (opModeIsActive()) {
            x.setPower(gamepad1.left_stick_y);
        }
    }
}
