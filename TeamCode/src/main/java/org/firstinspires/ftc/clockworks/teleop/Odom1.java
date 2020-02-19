package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.clockworks.algorithm.odometry.HomeMadeOdometry;
import org.firstinspires.ftc.clockworks.algorithm.odometry.WizardEXEOdometry;


@TeleOp(name = "Odom1", group = "Drive")
public class Odom1 extends LinearOpMode {
    private DcMotor mid;
    private DcMotor left;
    private DcMotor right;
    private WizardEXEOdometry o1 = new WizardEXEOdometry();

    @Override
    public void runOpMode() {
        mid = hardwareMap.get(DcMotor.class, "mid");
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");

        waitForStart();

        try {
            while (opModeIsActive()) {
                int midf = mid.getCurrentPosition();
                int leftf = left.getCurrentPosition();
                int rightf = right.getCurrentPosition();

                telemetry.addLine().addData("mid: ", midf);
                telemetry.addLine().addData("left: " , leftf);
                telemetry.addLine().addData("right: " , rightf);

                o1.feed(leftf, rightf, midf);

                telemetry.addLine().addData("X value: ", o1.getX());
                telemetry.addLine().addData("Y value: ", o1.getY());

                telemetry.update();
            }
        } catch(Exception e){
            telemetry.addLine().addData("Exceeption ", e.toString());
            telemetry.update();
            while (true) { }
        }

    }}