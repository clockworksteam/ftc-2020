package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor ml;
    private DcMotor mr;

    public void init(HardwareMap hardwareMap, String basename) {
        ml = hardwareMap.get(DcMotor.class, basename+"-left");
        mr = hardwareMap.get(DcMotor.class, basename+"-right");

        ml.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ml.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ml.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ml.setDirection(DcMotorSimple.Direction.FORWARD);
        ml.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    public void mergi() {
        ml.setPower(1);
        mr.setPower(1);

    }

    public void UNOmergi() {
        ml.setPower(-1);
        mr.setPower(-1);

    }

    public void stop() {
        ml.setPower(0);
        mr.setPower(0);
    }
}
