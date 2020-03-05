package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

public class BackServos implements Fiber {
    private Servo s;
    private Servo d;
    private boolean servosFree = true;
    private boolean l;

    private int sp = 0;
    private int sl = 0;
    private int dp = 0;
    private int dl = 0;

    public void initData(HardwareMap hardwareMap, String basename, boolean left) {
        s = hardwareMap.get(Servo.class, basename + "_left");
        d = hardwareMap.get(Servo.class, basename + "_right");

        s.setDirection(Servo.Direction.FORWARD);
        d.setDirection(Servo.Direction.REVERSE);

        l = left;
            s.setPosition(0.95);//0.95
            d.setPosition(0.99);

    }


    @Override
    public void init(InternalScheduler scheduler) {
        s.setDirection(Servo.Direction.FORWARD);
        d.setDirection(Servo.Direction.REVERSE);

    }

    @Override
    public void tick() {
        if (servosFree) {
            s.setPosition(sp);//0.85
            d.setPosition(dp);
            servosFree = false;
        } else {
            s.setPosition(sl);
            d.setPosition(dl);
            servosFree = true;
        }

    }

    @Override
    public void deinit() {

    }
}
