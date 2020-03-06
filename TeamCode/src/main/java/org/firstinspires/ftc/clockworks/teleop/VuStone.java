package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.vision.VuStoneLocalizer;


/**
 * This OP mode is used for implementing the algorithms written in the "VuStoneLocalizer" class.
 * It is a simple program that shows the relative y-axis coordinate on the phone's screen.
 */

@TeleOp(name = "VuStone", group = "Drive")
public class VuStone extends LinearOpMode {
    @Override
    public void runOpMode() {
        VuStoneLocalizer localizer = new VuStoneLocalizer();

        localizer.init(hardwareMap);

        waitForStart();

        /**
         * Here we create the instance of our localizer and we pass the hardwareMap object as the "VuStoneLocalizer" needs it in order to make the connection between the camera and the code.
         * The last line waits for the driver to press the "START" button on the phone's screen.
         */

        while (opModeIsActive()) {
            localizer.update();
            if (localizer.isVisible()) {
                telemetry.addData("Skystone:", localizer.getPosition());
            }
            telemetry.update();

            /**
             * This loop represents our OP mode. The first line updates our localizer in order for it to detect if there is a change in the relative positions of the Skytones captured by the camera.
             * After that, there is an "if" condition that verifies if there is a Skystone available in the frames captured by the phone's camera. If there is one, we will show its relative y-axis coordinate on the phone's screen using telemetry.
             * The last line updates the telemetry in order for the new text to appear on the phone's screen.
             */
        }

        localizer.stop();
        /**
         *
         * This line stops our localizer after the OP Mode has finished.
         */
    }
}
