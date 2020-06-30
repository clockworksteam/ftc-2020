package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.algorithm.motion.MotionCommander;
import org.firstinspires.ftc.clockworks.algorithm.motion.Point;
import org.firstinspires.ftc.clockworks.algorithm.motion.Trace;
import org.firstinspires.ftc.clockworks.control.Odometry;
import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.Intake;
import org.firstinspires.ftc.clockworks.scheduler.ExternalScheduler;
import org.firstinspires.ftc.clockworks.scheduler.OnDemandScheduler;

import java.util.ArrayList;

/**
 * TeleOP test for intake class.
 */

@TeleOp
public class IntakeOP extends LinearOpMode {
    private GamepadInput controller = new GamepadInput();
    private Intake intake = new Intake();


    @Override
    public void runOpMode() {

  //      ints.stream().filter(i -> i < 56).sorted().mapToDouble().map(d -> d / 3).allMatch(d % 2 == 6);

        intake.initData(hardwareMap, "IN");

        waitForStart();


        while (opModeIsActive()){
            if(gamepad1.a) intake.forward();

            if(gamepad1.b) intake.reverse();

            if(gamepad1.x) intake.stop();
        }

    }

}
