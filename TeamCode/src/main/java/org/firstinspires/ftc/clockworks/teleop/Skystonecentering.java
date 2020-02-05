package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;
import org.firstinspires.ftc.clockworks.helpers.AngleHelper;
import org.firstinspires.ftc.clockworks.algorithm.PID;
import org.firstinspires.ftc.clockworks.algorithm.SkystonedettectXY;

import java.util.List;

@Autonomous(name = "Skystonecentering", group = "Drive")
public class Skystonecentering extends LinearOpMode {
    private SkystonedettectXY skystonedettectXY = new SkystonedettectXY();
    private MecanumDriver wheels = new MecanumDriver();
    private PID pid = new PID(0.02, 0, 0.01);//todo setat constante
    private IMUSensor gyro = new IMUSensor();
    private PositionController positionController = new PositionController();
    private ElapsedTime runtime = new ElapsedTime();
    private PID pid2 = new PID(0.015 , 0.0, 0.0);//todo setat constante

    /**
     * Here we create the objects (including 2 PID controllers) of the classes in which we have written our algorithms detailed earlier.
     * We need 2 PID controllers because there are 2 different types of navigation(the one in which robot rotates and the one in which robot is moving forward).
     * we decided to set the constants as this: the first proportional constant to 0.02, the first derivative constant to 0.01 and the second proportional constant to 0.015
     */


    private double zeroHeading = 0;
    private double correction;

    List<Float> distx;
    List<Float> disty;

    private float dx, dy;
    /**
     * Some array and variables which will be used later in this TeleOP. "zeroHeading" will represent the initial heading robot is having when it is initialised by the driver, "correction" will represent the correction calculated by the PID controller, and it should be applied to the motors. "distx" and "disty" are 2 arrays that will store the information given by the SkystonedettectXY class (2-axis projection of the distance from the center to the nearest stones)
     * "dx" and "dy" will store the 2-axis projection of the distance from the center to the nearest STONE (of any type)
     */

    @Override
    public void runOpMode(){
        /**
         * This method is automatically called by the program when the driver press the "INIT" button on the Driver Station
         */


        telemetry.addData("Status", "Initializing MecanumTeleOp");
        telemetry.update();
        skystonedettectXY.init(telemetry);
        wheels.init(telemetry, hardwareMap, "ok");
        gyro.init (telemetry, hardwareMap, "imu");
        positionController.init(telemetry , wheels, gyro);
        /**
         * Here our algorithms are set up for the current robot configuration. Basically, "hardwareMap" (the object that helps us map the code to their corresponding name in code) and "telemetry" (the object that helps us to debug our program by giving us the possibility to show some text on the Driver Station console) are passed through the objects which represent our classes.
         */

        waitForStart();
        runtime.reset();

        /**
         * Here we wait the driver to press the "Start" button
         */

        while(opModeIsActive()) {
            pid.setTarget(0);

            /**
             * Because this code was a prototype, it does not follow the FTC rules for stone placement. Here we assume that when we start the autonomous the captured area of the camera contains only one Skystone (and no other stones around it).
             * As a test, we wanted to rotate to the Skystone and navigate next to it.
             * This loop starts as all our usual loops. The first lines update the "gyro", then they store the projection on the x axis of the distance from the center of the camera to the center of the Skystone in the variable "dx".There is also another loop which waits for the TensorFlow to find a Skystone.
             * The last lines try to rotate the robot toward the Skystone using the correction returned by the first PID controller.
             * This loop should end when we achieve a very small distance from the center of the image to the center of the Skystone.
             */
            do {
                gyro.readDevice();
                distx = skystonedettectXY.updateTF().distX();
                while (distx.isEmpty()) {
                    gyro.readDevice();
                    distx = skystonedettectXY.updateTF().distX();
                    positionController.blindRotate(0).update();
                }
                dx = distx.get(0);
                correction = pid.feed(-dx, System.currentTimeMillis() / 1000.0);
                positionController.blindRotate(correction).update();
                correction = pid.feed(0, System.currentTimeMillis() / 1000.0);

                telemetry.update();
                distx = skystonedettectXY.updateTF().distX();


            } while (Math.abs(dx) < 180 && opModeIsActive());


            /**
             * Here we assume that the robot is already placed with its front to the direction of the Skystone, as the part in which Dorel rotates should have already finished.
             * We get the direction
             */


            disty = skystonedettectXY.updateTF().distY();
            while (disty.isEmpty()) disty = skystonedettectXY.updateTF().distY();
            dy = disty.get(0);

            positionController.setHeading(0);
            positionController.setDirection(0 , 0.8);

            pid2.setTarget(-600);

           while (opModeIsActive() && Math.abs(dy) < 640) {
                disty = skystonedettectXY.updateTF().distY();
                while (disty.isEmpty()) {
                    disty = skystonedettectXY.updateTF().distY();
                    positionController.setDirection(0, 0);
                    positionController.update();
                }
                dy = disty.get(0);
                correction = pid2.feed(dy, System.currentTimeMillis() / 1000.0);
                positionController.setDirection(0, correction).update();
            }
        }
        skystonedettectXY.TFclose();
    }}






