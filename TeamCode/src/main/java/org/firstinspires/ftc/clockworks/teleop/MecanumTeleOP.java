package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.clockworks.hardware.BackServos;
import org.firstinspires.ftc.clockworks.hardware.test.Gripper;
import org.firstinspires.ftc.clockworks.hardware.Gripping;
import org.firstinspires.ftc.clockworks.helpers.AngleHelper;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;
import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.control.PositionController;




/**
 * TeleOP class for movement with the Mecanum Wheels.
 * It implements the PositionController class which was meant to be used with all the algorithms for the Mecanum Wheels.
 * Running this OP requires 4 motors to be connected and configured with the Expansion Hub.
 */
@TeleOp (name = "MecanumTeleOP", group = "Drive")
public class MecanumTeleOP extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private GamepadInput controller = new GamepadInput();
    private MecanumDriver wheels = new MecanumDriver();
    private IMUSensor gyro = new IMUSensor();
    private PositionController positionController = new PositionController();
    private double zeroHeading = 0;
    private Gripping gripper = new Gripping();
    private boolean closed = false;

    private  boolean xState = false;
    private boolean bState = false;
    private boolean aState = false;

    private boolean rotated = false;
    private BackServos servo = new BackServos();


    /**
     * Method for running OpMode.
     * This method is called when the "INIT" button is pressed.
    */
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing MecanumTeleOp");
        telemetry.update();
        wheels.init(telemetry, hardwareMap, "ok");
        gyro.init (telemetry, hardwareMap, "imu");
        positionController.init(telemetry, wheels, gyro);
       // servo.init(hardwareMap, "servo", true);
       // gripper.init(hardwareMap, "Gripper");



        waitForStart();
        runtime.reset();

        controller.init(gamepad1);

        gyro.readDevice();
        zeroHeading = gyro.getHeading();
        while (opModeIsActive()) {
            controller.readDevice();
            gyro.readDevice();

            if (gamepad1.dpad_up) {
                //gripper.liftUp();
            }
            if (gamepad1.dpad_down) {
                //gripper.liftDown();
            }
            if (!gamepad1.dpad_up && !gamepad2.dpad_down) {
               // gripper.liftLock();
            }

           /* if (xState != gamepad1.x && gamepad1.x) {
                closed = !closed;
                if (closed) {
                    gripper.closeFront();
                } else {
                    gripper.openFront();
                }
            }*/
            xState = gamepad1.x;

            if(bState != gamepad1.b && gamepad1.b) {
                //servo.prins();
            }
            bState = gamepad1.b;

            if (!rotated && gamepad1.left_bumper) {
                //gripper.paralelrotationfront();
                rotated = true;
            }

            if (gamepad1.a && aState != gamepad1.a) {

                zeroHeading = zeroHeading+180;
                zeroHeading = AngleHelper.norm(zeroHeading);
            }

            if (gamepad1.right_bumper){

                zeroHeading = gyro.getHeading();


            }
            aState = gamepad1.a;

            if (controller.getAmplitudeRight() > 0.1) {
                positionController.blindRotate(-gamepad1.right_stick_x / 2);
            } else {
                if (positionController.isBlindRotating()) {
                    positionController.setHeading(gyro.getHeading());
                }
            }

            double turbo;
            if (gamepad1.left_trigger > 0) turbo = 1;
            else turbo = 0.5;

            double walkAngle = AngleHelper.norm(controller.getAngleLeft() + zeroHeading);
            positionController.setDirection(walkAngle, controller.getAmplitudeLeft() * turbo);
            positionController.update();

            telemetry.addLine().addData("Speed: ", controller.getAmplitudeLeft());
            telemetry.addLine().addData("Direction: ", controller.getAngleLeft());
            telemetry.addLine().addData("Heading:", controller.getAngleRight());
            telemetry.update();
        }

    }
}






