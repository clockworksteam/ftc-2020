package org.firstinspires.ftc.clockworks.algorithm;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;
import org.firstinspires.ftc.clockworks.helpers.AngleHelper;
import org.firstinspires.ftc.clockworks.algorithm.PID;
import org.firstinspires.ftc.clockworks.algorithm.SkystonedettectXY;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Stonecentermovement {
    private SkystonedettectXY skystonedettectXY = new SkystonedettectXY();
    private MecanumDriver wheels = new MecanumDriver();
    private PID pid = new PID(0.02, 0, 0.01);
    private IMUSensor gyro = new IMUSensor();
    private PositionController positionController = new PositionController();
    private double zeroHeading = 0;
    private ElapsedTime runtime = new ElapsedTime();
    private double correction;
    private PID pid2 = new PID(0.015 , 0.0, 0.0);
    private Telemetry telemetry;
    List<Float> distx;
    List<Float> disty;

    private float dx, dy;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        telemetry.update();
        skystonedettectXY.init(hardwareMap, telemetry);
        wheels.init(telemetry, hardwareMap, "ok");
        gyro.init (telemetry, hardwareMap, "imu");
        positionController.init(telemetry , wheels, gyro);
    }

    public void dute() {
        pid.setTarget(0);
        /*
         * Rotate towards skystone
         */
        do {
            gyro.readDevice();
            distx = skystonedettectXY.updateTF().distX();
            int n = 0;
            while (distx.isEmpty()) {
                gyro.readDevice();
                correction = pid.feed(0, System.currentTimeMillis() / 1000.0);
                distx = skystonedettectXY.updateTF().distX();
                positionController.blindRotate(0).update();
            }
            dx = distx.get(0);
            correction = pid.feed(-dx, System.currentTimeMillis() / 1000.0);
            positionController.blindRotate(correction).update();
            telemetry.update();
            distx = skystonedettectXY.updateTF().distX();


        } while (Math.abs(dx) < 360);


        disty = skystonedettectXY.updateTF().distY();
        while (disty.isEmpty()) disty = skystonedettectXY.updateTF().distY();
        dy = disty.get(0);

        positionController.setHeading(0);
        ////////positionController.setDirection(0 , 0.8);

        //pid2.setTarget(-600);

        while (Math.abs(dy) < 640) {//todo: set the value
            disty = skystonedettectXY.updateTF().distY();
            while (disty.isEmpty()) {
                disty = skystonedettectXY.updateTF().distY();
                positionController.setDirection(0, 0);
                positionController.update();
            }
            dy = disty.get(0);
            correction = pid2.feed(dy, System.currentTimeMillis() / 1000.0);
            positionController.setDirection(0, correction).update();

            telemetry.update();

        }
    }
        //skystonedettectXY.TFclose();
    }
