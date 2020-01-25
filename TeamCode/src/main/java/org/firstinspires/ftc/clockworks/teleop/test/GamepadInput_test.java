package org.firstinspires.ftc.clockworks.teleop.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.hardware.GamepadInput;

@TeleOp
public class GamepadInput_test extends LinearOpMode {
    GamepadInput gamepadInput = null;


    @Override
    public void runOpMode() throws InterruptedException {
        gamepadInput = new GamepadInput();

        gamepadInput.init(gamepad1 , gamepadInput);


        waitForStart();
        while (opModeIsActive()){
            if (gamepadInput.was_pressed("A")) System.out.println("Pressed");
            if (gamepadInput.was_released("A")) System.out.println("Released");


            telemetry.addLine().addData("Amplitude Left: " , gamepadInput.getAmplitudeLeft());
            telemetry.addLine().addData("Amplitude Right" , gamepadInput.getAmplitudeRight());
            telemetry.addLine().addData("Angle Left" , gamepadInput.getAngleLeft());
            telemetry.addLine().addData("Angle Right" , gamepadInput.getAngleRight());


        }

        gamepadInput.set_thread_stat(false);


    }
}
