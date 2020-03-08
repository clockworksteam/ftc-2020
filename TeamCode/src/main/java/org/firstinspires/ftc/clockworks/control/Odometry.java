package org.firstinspires.ftc.clockworks.control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.clockworks.algorithm.odometry.TriOdometry;
import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

/**
 * For this season of FTC we decided to use odometers. They are some small wheels located under our robot that are mounted in that way so they can touch the ground. They measure how many times they had rotate upon a given time, so we can use them in order to get the position of our robot in a cartesian coordinate system.
 * In this season of FTC we are using 3 odometers.
 *
 */

public class Odometry implements Fiber {
    private final TriOdometry processor;
    private final DcMotor left, right, mid;
    private volatile double x, y, orientation;

    public Odometry(TriOdometry processor, HardwareMap hardwareMap, String left, String right, String mid) {
        this.processor = processor;
        this.left = hardwareMap.get(DcMotor.class, left);
        this.right = hardwareMap.get(DcMotor.class, right);
        this.mid = hardwareMap.get(DcMotor.class, mid);
    }

    @Override
    public void init(InternalScheduler scheduler) {

    }

    @Override
    public void tick() {
        int left = this.left.getCurrentPosition();
        int right = this.right.getCurrentPosition();
        int mid = this.mid.getCurrentPosition();

        processor.feed(left, right, mid);

        x = processor.getX();
        y = processor.getY();
        orientation = processor.getOrientation();
    }

    @Override
    public void deinit() {

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getOrientation() {
        return orientation;
    }
}
