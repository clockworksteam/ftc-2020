package org.firstinspires.ftc.clockworks.teleop.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.clockworks.algorithm.PID;
import org.firstinspires.ftc.clockworks.algorithm.SkystonedettectXY;
import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;

import java.util.List;

@TeleOp(name  = "CenterStone", group = "Drive")
public class CenterStone extends LinearOpMode {
    private SkystonedettectXY stonelist = new SkystonedettectXY();
    private MecanumDriver driver = new MecanumDriver();
    private PID pid = new PID(0.0015 , 0.0, 0.0);
    private IMUSensor gyro = new IMUSensor();
    private PositionController controller = new PositionController();
    private ElapsedTime runtime = new ElapsedTime();
    private double correction;

    List<Float> distx;
    List<Float> disty;
    private double dx, dy;

    @Override
    public void runOpMode() {
        while(opModeIsActive()) {
            telemetry.addData("Status", "Initializing MecanumTeleOp");
            telemetry.update();
            stonelist.init(hardwareMap, telemetry);
            driver.init(telemetry, hardwareMap, "ok");
            gyro.init(telemetry, hardwareMap, "imu");
            controller.init(telemetry, driver, gyro);

            waitForStart();
            runtime.reset();
            pid.setTarget(0);

            gyro.readDevice();
            distx = stonelist.updateTF().distX();
            if (distx.isEmpty()) {
                continue;

            }else {
                dx = distx.get(0);
                while(dx != 0) {
                    controller.blindRotate(dx/180);
                    controller.update();
                    distx = stonelist.updateTF().distX();
                    dx = distx.get(0);
                }
            }


        }
    }
}
