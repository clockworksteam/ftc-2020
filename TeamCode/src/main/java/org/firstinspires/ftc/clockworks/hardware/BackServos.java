package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class BackServos {
    private Servo s;
    private Servo d;
    private boolean servosFree = true;
    private boolean l;

    public void init(HardwareMap hardwareMap, String basename, boolean left) {
        s = hardwareMap.get(Servo.class, basename + "_left");
        d = hardwareMap.get(Servo.class, basename + "_right");

        s.setDirection(Servo.Direction.FORWARD);
        d.setDirection(Servo.Direction.REVERSE);

        l = left;
            s.setPosition(0.95);//0.95
            d.setPosition(0.99);

    }

    public void prins() {
        if (servosFree) {
            s.setPosition(0.4);//0.85
            d.setPosition(0.6);
            servosFree = false;
        } else {
                s.setPosition(0.95);
                d.setPosition(0.99);
            servosFree = true;
        }
    }

}
