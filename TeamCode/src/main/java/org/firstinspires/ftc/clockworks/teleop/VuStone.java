package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.vision.VuStoneLocalizer;

@TeleOp(name = "VuStone", group = "Drive")
public class VuStone extends LinearOpMode {
    @Override
    public void runOpMode() {
        VuStoneLocalizer localizer = new VuStoneLocalizer();

        localizer.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            localizer.update();
            if (localizer.isVisible()) {
                telemetry.addData("Skystone:", localizer.getPosition());
            }
            telemetry.update();
        }

        localizer.stop();
    }
}
