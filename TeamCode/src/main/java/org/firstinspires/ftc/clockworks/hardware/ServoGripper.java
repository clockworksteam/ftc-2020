package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ServoGripper {
    Telemetry telemetry;

    Servo servo0 = null;
    Servo servo1 = null;
    Servo servo2 = null;
    Servo servo3 = null;


    public void init (HardwareMap hardwareMap , Telemetry telemetry){
        servo0 = hardwareMap.get(Servo.class , "servo0");
        servo1 = hardwareMap.get(Servo.class , "servo1");
        servo2 = hardwareMap.get(Servo.class , "servo2");
        servo3 = hardwareMap.get(Servo.class , "servo3");
    }


    public void open_gripper (){
        servo0.setPosition(0.35);
        servo1.setPosition(0.65);
        servo2.setPosition(0.35);
        servo3.setPosition(0.65);

    }


    public void close_gripper (){
        servo0.setPosition(0.5);
        servo1.setPosition(0.5);
        servo2.setPosition(0.5);
        servo3.setPosition(0.5);

    }
}
