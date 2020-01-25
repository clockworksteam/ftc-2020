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
    private HomeMadeOdometry o0 = new HomeMadeOdometry();
    private WizardEXEOdometry o1 = new WizardEXEOdometry();
    private int ododo = 0;
    private int t = 0;

    private int midf = 0;
    private int rightf = 0;
    private int leftf = 0;

    private int mF = 0;
    private int rF = 0;
    private int lF = 0;

    @Override
    public void runOpMode() {
        mid = hardwareMap.get(DcMotor.class, "mid");
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
       // dm = hardwareMap.get(DcMotor.class, "qwe");
        if (ododo == 0) o0.init(288, 25, 9);
        else o1.init(288, 25, 9);

        waitForStart();

        while (opModeIsActive()) {
            midf = mid.getCurrentPosition();
            leftf = left.getCurrentPosition();
            rightf = right.getCurrentPosition();
            if (ododo == 0) {

                o0.feed(leftf - lF, rightf - rF, midf - mF);

                telemetry.addLine().addData("X value: ", o0.getX());
                telemetry.addLine().addData("Y value: ", o0.getY());
                telemetry.addLine().addData("Angle value: ", o0.getAngle());
            } else {
                o1.feed(leftf - lF, rightf - rF, midf - mF);
                telemetry.addLine().addData("X value: ", o1.getX());
                telemetry.addLine().addData("Y value: ", o1.getY());
            }
            mF = midf;
            rF = rightf;
            lF = leftf;
            telemetry.update();
        }
    }}