package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.clockworks.algorithm.AngularPID;
import org.firstinspires.ftc.clockworks.algorithm.SkystonedettectXY;
import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.hardware.BackServos;
import org.firstinspires.ftc.clockworks.hardware.Gripping;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;
import org.firstinspires.ftc.clockworks.helpers.AngleHelper;

import java.util.concurrent.atomic.AtomicLong;

@Autonomous(name = "AutonomDemoBlue", group = "Drive")
public class AutonomDemoBlue extends LinearOpMode {

    private BackServos backServos = new BackServos();
    private MecanumDriver mecanumDriver = new MecanumDriver();
    private PositionController positionController = new PositionController();
    private IMUSensor gyro = new IMUSensor();
    private Gripping gripping = new Gripping();


    private SkystonedettectXY skystonedettectXY = new SkystonedettectXY();
    static  boolean found;

    private double zeroHeading = 0;

    private double getRelativeOrientation(double gyroAngle) {
        return AngleHelper.norm(gyroAngle + zeroHeading);
    }

    @Override
    public void runOpMode() {


        backServos.init(hardwareMap, "servo", true);
        mecanumDriver.init(telemetry, hardwareMap, "ok");
        positionController.init(telemetry, mecanumDriver, gyro);
        gripping.init(hardwareMap, "Gripper");
        gyro.init(telemetry, hardwareMap, "imu");
        gyro.readDevice();
        zeroHeading = gyro.getHeading();

        waitForStart();


        //center on the foundation
        rampSpeed(getRelativeOrientation(90), 0, 0.6, 300);
        sleepAndUpdate(1000);
        positionController.setDirection(0, 0);

        // Go to foundation
        rampSpeed(getRelativeOrientation(0), 0, 0.6, 300);
        sleepAndUpdate(2500);
        positionController.setDirection(0, 0);

        // Latch onto foundation and wait a moment for servos to engage
        backServos.prins();
        sleepAndUpdate(500);

        // Slowly pull
        positionController.setDirection(getRelativeOrientation(180), 0.7);
        sleepAndUpdate(2500);

        // rotate
        positionController.setHeading(getRelativeOrientation(90));
        positionController.setDirection(0, 0);
        sleepAndUpdate(2000);

        // go to tava center
        positionController.setDirection(getRelativeOrientation(0), 0.4);
        sleepAndUpdate(300);


        // Push Foundation into corner
        rampSpeed(getRelativeOrientation(90), 0, 0.65, 600);
        sleepAndUpdate(2000);



        positionController.setDirection(getRelativeOrientation(0) , 0.8);
        sleepAndUpdate(150);
        backServos.prins();
        sleepAndUpdate(450);




        positionController.setDirection(getRelativeOrientation(0), 0);

        rampSpeed(getRelativeOrientation(-90), 0, 1, 600);
        sleepAndUpdate(1400);

        positionController.setHeading(getRelativeOrientation(90));
        positionController.setDirection(0, 0);
        sleepAndUpdate(600);

        // TODO: Fetch a skystone
    }

    /**
     * Sleep the current thread while also updating the motors and gyro sensor.
     * The sleep and update cycle has a granularity of 10 milliseconds, to avoid over-updating the motors.
     *
     * @param millis the amount of time to sleep
     */
    private void sleepAndUpdate(long millis) {
        long stop = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < stop && opModeIsActive()) {
            gyro.readDevice();
            positionController.update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // nothing
            }
        }
    }

    /**
     * Ramp the speed over a specified time duration. Function blocks execution until ramp is done.
     * The update cycle has a granularity of 10 milliseconds, to avoid over-updating the motors.
     * Use this functionality while carrying game elements to avoid damage or accidental dropping.
     *
     * @param direction the angle of transportation
     * @param fromPower the speed at initial moment
     * @param toPower   the speed at final moment
     * @param millis    duration of the ramp
     */
    private void rampSpeed(double direction, double fromPower, double toPower, long millis) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + millis && opModeIsActive()) {
            gyro.readDevice();
            positionController.setDirection(direction, Range.scale(System.currentTimeMillis(), start, start + millis, fromPower, toPower));
            positionController.update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // nothing
            }
        }
    }
}
