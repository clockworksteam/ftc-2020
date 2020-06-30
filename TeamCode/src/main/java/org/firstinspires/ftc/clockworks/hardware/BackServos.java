package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;

/**
 * This class is used to control the servos meant to grip the foundation and move it. It implements the
 * Fiber interface in order to use the scheduler API.
 */


public class BackServos implements Fiber {
    private Servo s;
    private Servo d;
    private boolean servosFree = true;


    private int sp = 0;
    private int sl = 0;
    private int dp = 0;
    private int dl = 0;

    /**
     * Basic "init" method for setting the properties of the servos.
     * @param hardwareMap
     * @param basename
     */

    public void initData(HardwareMap hardwareMap, String basename) {
        s = hardwareMap.get(Servo.class, basename + "_left");
        d = hardwareMap.get(Servo.class, basename + "_right");

        s.setDirection(Servo.Direction.FORWARD);
        d.setDirection(Servo.Direction.REVERSE);

            s.setPosition(0.95);//0.95
            d.setPosition(0.99);

    }

    /**
     * This method initializes the scheduler API
     * @param scheduler the scheduler API
     */

    @Override
    public void init(InternalScheduler scheduler) {
        s.setDirection(Servo.Direction.FORWARD);
        d.setDirection(Servo.Direction.REVERSE);

    }


    /**
     * Changing the servo status.
     * This can close and open the grip.
     */
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
