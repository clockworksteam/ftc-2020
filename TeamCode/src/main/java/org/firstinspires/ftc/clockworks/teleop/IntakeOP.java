package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.Intake;

@TeleOp(name = "Intake", group = "Drive")
public class IntakeOP extends LinearOpMode {
    private GamepadInput controller = new GamepadInput();
    private Intake intake = new Intake();



    @Override
    public void runOpMode() {
        intake.init(hardwareMap, "IN");
        controller.init(gamepad1, controller);

        waitForStart();

        if(gamepad1.a) {
            intake.mergi();
        }else if(gamepad1.b) {
            intake.UNOmergi();
        }else if(gamepad1.x) {
            intake.stop();
        }

    }

}
