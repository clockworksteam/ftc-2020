package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class ServoGripper extends LinearOpMode {
    Servo left1 = null;
    Servo left2 = null;
    Servo right1 = null;
    Servo right2 = null;


    @Override
    public void runOpMode() throws InterruptedException {
        left1 = hardwareMap.get(Servo.class , "left1");
        left2 = hardwareMap.get(Servo.class , "left2");
        right1 = hardwareMap.get(Servo.class , "right1");
        right2 = hardwareMap.get(Servo.class , "right2");

        waitForStart();

     //   left1.setPosition(0.33);
 //     left2.setPosition(0.8);
        right1.setPosition(0.0);
   //     right2.setPosition(0.22);

        Thread.sleep(3000);

        System.out.println ("lef1: " + left1.getPosition());
        System.out.println ("left2: " + left2.getPosition());
        System.out.println ("right1: " + right1.getPosition());
        System.out.println ("right2: " + right2.getPosition());


        Thread.sleep(3000);

    }
}
