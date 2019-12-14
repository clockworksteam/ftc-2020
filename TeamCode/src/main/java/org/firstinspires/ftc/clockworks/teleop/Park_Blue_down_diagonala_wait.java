package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.clockworks.algorithm.SkystonedettectXY;
import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;


@Autonomous(name = "Park_Blue_down_diagonala_wait", group = "Drive")
public class Park_Blue_down_diagonala_wait extends LinearOpMode {
    private MecanumDriver mecanumDriver = new MecanumDriver();

    private PositionController positionController = new PositionController();
    private IMUSensor imuSensor = new IMUSensor();
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        mecanumDriver.init(telemetry, hardwareMap, "ok");
        imuSensor.init(telemetry, hardwareMap, "imu");
        positionController.init(telemetry, mecanumDriver, imuSensor);

        waitForStart();
        runtime.reset();

        imuSensor.readDevice();
        sleepAndUpdate(25000);


        imuSensor.readDevice();
        positionController.setDirection(-90, 0.8);
        sleepAndUpdate(1700);

        positionController.setDirection(0,0.8);
        sleepAndUpdate(1000);

        positionController.setDirection(0, 0);

    }


    private void sleepAndUpdate(long millis) {
        long stop = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < stop && opModeIsActive()) {
            imuSensor.readDevice();
            positionController.update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // nothing
            }
        }
    }
}