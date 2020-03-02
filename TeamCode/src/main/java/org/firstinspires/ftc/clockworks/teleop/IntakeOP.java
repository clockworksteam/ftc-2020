package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.Intake;

@TeleOp
public class IntakeOP extends LinearOpMode {
    private GamepadInput controller = new GamepadInput();
    private Intake intake = new Intake();



    @Override
    public void runOpMode() {
        intake.init(hardwareMap, "IN");

        waitForStart();

        while (opModeIsActive()){
            if(gamepad1.a) intake.mergi();

            if(gamepad1.b) intake.UNOmergi();

            if(gamepad1.x) intake.stop();
        }

    }

}
