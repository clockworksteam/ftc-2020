package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

public class Intake implements Fiber {

    private DcMotor ml;
    private DcMotor mr;

    volatile int dir = 0;
    int currentDir = 0;

    @Override
    public void init(InternalScheduler scheduler) {
        ml.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ml.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ml.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ml.setDirection(DcMotorSimple.Direction.FORWARD);
        ml.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void tick() {
        if (dir != currentDir) {
            currentDir = dir;
            mr.setPower(currentDir);
            ml.setPower(currentDir);
        }
    }

    @Override
    public void deinit() {
        // nothing
    }

    public void initData(HardwareMap hardwareMap, String basename) {
        ml = hardwareMap.get(DcMotor.class, basename+"-left");
        mr = hardwareMap.get(DcMotor.class, basename+"-right");
    }


    public void forward() {
        dir = 1;

    }

    public void reverse() {
        dir = -1;

    }

    public void stop() {
        dir = 0;
    }
}
