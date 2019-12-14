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

@TeleOp(name = "Skystonecentering", group = "Drive")
public class Skystonecentering extends LinearOpMode {
    private SkystonedettectXY skystonedettectXY = new SkystonedettectXY();
    private MecanumDriver wheels = new MecanumDriver();
    private PID pid = new PID(0.02, 0, 0.01);//todo setat constante
    private IMUSensor gyro = new IMUSensor();
    private PositionController positionController = new PositionController();
    private double zeroHeading = 0;
    private ElapsedTime runtime = new ElapsedTime();
    private double correction;
    private PID pid2 = new PID(0.015 , 0.0, 0.0);//todo setat constante

    List<Float> distx;
    List<Float> disty;

    private float dx, dy;

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initializing MecanumTeleOp");
        telemetry.update();
        skystonedettectXY.init(hardwareMap, telemetry);
        wheels.init(telemetry, hardwareMap, "ok");
        gyro.init (telemetry, hardwareMap, "imu");
        positionController.init(telemetry , wheels, gyro);

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {
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
                    distx = skystonedettectXY.updateTF().distX();
                    positionController.blindRotate(0).update();
                   // telemetry.addLine().addData("waiting", n++);
                }
                dx = distx.get(0);
                correction = pid.feed(-dx, System.currentTimeMillis() / 1000.0);
                positionController.blindRotate(correction).update();
                correction = pid.feed(0, System.currentTimeMillis() / 1000.0);
                //telemetry.addData("dx", dx);
                //telemetry.addData("correction", correction);
                telemetry.update();
                distx = skystonedettectXY.updateTF().distX();


            } while (Math.abs(dx) < 360 && opModeIsActive());


            disty = skystonedettectXY.updateTF().distY();
            while (disty.isEmpty()) disty = skystonedettectXY.updateTF().distY();
            dy = disty.get(0);

            positionController.setHeading(0);
            ////////positionController.setDirection(0 , 0.8);

            //pid2.setTarget(-600);

           while (opModeIsActive() && Math.abs(dy) < 640) {//todo: set the value
                disty = skystonedettectXY.updateTF().distY();
                while (disty.isEmpty()) {
                    disty = skystonedettectXY.updateTF().distY();
                    positionController.setDirection(0, 0);
                    positionController.update();
                }
                dy = disty.get(0);
                correction = pid2.feed(dy, System.currentTimeMillis() / 1000.0);
                positionController.setDirection(0, correction).update();
                telemetry.addData("dy", dy);
                telemetry.addData("correction", correction);
                telemetry.update();

            }
        }
        skystonedettectXY.TFclose();
    }}






