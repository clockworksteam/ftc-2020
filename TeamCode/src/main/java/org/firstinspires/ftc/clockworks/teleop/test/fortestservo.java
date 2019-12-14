package org.firstinspires.ftc.clockworks.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.algorithm.SkystonedettectXY;
import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.hardware.BackServos;
import org.firstinspires.ftc.clockworks.hardware.Gripping;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;
import org.firstinspires.ftc.clockworks.helpers.AngleHelper;

@TeleOp(name = "fortestservo" , group = "Drive")
public class fortestservo extends LinearOpMode {

    private BackServos backServos = new BackServos();
    private MecanumDriver mecanumDriver = new MecanumDriver();
    private PositionController positionController = new PositionController();
    private IMUSensor gyro = new IMUSensor();
    private Gripping gripping = new Gripping();


    private SkystonedettectXY skystonedettectXY = new SkystonedettectXY();
    static boolean found;

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


        backServos.prins();
        try {
            Thread.sleep(1000);
        }catch (Exception e) {

        }

    }
}