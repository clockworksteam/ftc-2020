package org.firstinspires.ftc.clockworks.control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.clockworks.algorithm.odometry.TriOdometry;
import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;


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
