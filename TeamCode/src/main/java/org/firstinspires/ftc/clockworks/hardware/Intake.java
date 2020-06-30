package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

/**
 * The intake class is used to catch the stones. It too implements the Fiber interface.
 */

public class Intake implements Fiber {

    private DcMotor ml;
    private DcMotor mr;

    volatile int dir = 0;
    int currentDir = 0;

    /**
     * The "init" method sets up the motor's characteristics and the scheduler API.
     * @param scheduler the scheduler API
     */

    @Override
    public void init(InternalScheduler scheduler) {
        ml.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ml.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ml.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ml.setDirection(DcMotorSimple.Direction.FORWARD);
        ml.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    /**
     * Method for applying the desired move for the motors.
     */

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


    /**
     * Making the connection between the code and the motors.
     * @param hardwareMap
     * @param basename
     */

    public void initData(HardwareMap hardwareMap, String basename) {
        ml = hardwareMap.get(DcMotor.class, basename+"-left");
        mr = hardwareMap.get(DcMotor.class, basename+"-right");
    }


    /**
     * Sets the motors to rotate forward for catching the stones.
     */

    public void forward() {
        dir = 1;

    }


    /**
     * Sets the motors to move backwards for throwing the stones. Only for tests.
     */

    public void reverse() {
        dir = -1;

    }

    /**
     * Sets the motors to stop when the stones are already caught
     */

    public void stop() {
        dir = 0;
    }
}
