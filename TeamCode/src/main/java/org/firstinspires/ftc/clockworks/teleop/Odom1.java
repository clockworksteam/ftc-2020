package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.clockworks.algorithm.odometry.HomeMadeOdometry;
import org.firstinspires.ftc.clockworks.algorithm.odometry.WizardEXEOdometry;


@TeleOp(name = "Odom1", group = "Drive")
public class Odom1 extends LinearOpMode {
    private DcMotor motor;
    private HomeMadeOdometry o0 = new HomeMadeOdometry();
    private WizardEXEOdometry o1 = new WizardEXEOdometry();
    private int ododo = 1;
    private int fost = 0;

    private int t;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "Yuri");
       // dm = hardwareMap.get(DcMotor.class, "qwe");
        if (ododo == 0) o0.init(100, 300, 2.54);
        else o1.init(1120, 1, 2.54);

        waitForStart();

        while (opModeIsActive()) {
            t = motor.getCurrentPosition();
            if (ododo == 0) {
               /* o0.feed(0, 0, motor.getCurrentPosition() - fost);

                t = o0.getX();
                o0.coord_change(0, 0, 0);*/

                o0.feed(t - fost, t - fost, 0);
                //if(t == o0.getY()) telemetry.addLine().addData("Yes", "");
                //else telemetry.addLine().addData("No", "");

                telemetry.addLine().addData("X value: ", o0.getX());
                telemetry.addLine().addData("Y value: ", o0.getY());
                telemetry.addLine().addData("Absolute Angle value: ", o0.getAbsoluteAngle(t, t));
                telemetry.addLine().addData("Angle value: ", o0.getAngle());
                //telemetry.addLine().addData("D ", motor.getCurrentPosition()-fost);
            } else {
                o1.feed(t- fost, t- fost, 0);
                telemetry.addLine().addData("X value: ", o1.getX());
                telemetry.addLine().addData("Y value: ", o1.getY());
                //telemetry.addLine().addData("X value: ", o0.());
            }
            fost = t;
            telemetry.update();
        }
    }}