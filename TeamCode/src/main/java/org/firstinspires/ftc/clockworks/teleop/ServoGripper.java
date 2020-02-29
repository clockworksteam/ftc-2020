package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoGripper extends LinearOpMode {
    Servo servo0 = null;
    Servo servo1 = null;
    Servo servo2 = null;
    Servo servo3 = null;


    @Override
    public void runOpMode() throws InterruptedException {
        servo0 = hardwareMap.get(Servo.class , "servo0");
        servo1 = hardwareMap.get(Servo.class , "servo1");
        servo2 = hardwareMap.get(Servo.class , "servo2");
        servo3 = hardwareMap.get(Servo.class , "servo3");

        waitForStart();

        while (opModeIsActive()) {

            // public void closeGrip() {

            if (gamepad1.x) {
                servo0.setPosition(0.35);
                servo1.setPosition(0.65);
                servo2.setPosition(0.35);
                servo3.setPosition(0.65);
            }

            if (gamepad1.a){
                servo0.setPosition(0.5);
                servo1.setPosition(0.5);
                servo2.setPosition(0.5);
                servo3.setPosition(0.5);
            }
            //}

        /* public void openGrip() {
            servo0.setPosition(0.5 - 5.0 / 27);
            servo1.setPosition(0.5 + 5.0 / 27);
            servo2.setPosition(0.5 - 5.0 / 27);
            servo3.setPosition(0.5 + 5.0 / 27);
       // }*/
//        while (true) {
//            System.out.println ("servo1: " + servo0.getPosition());
//            System.out.println ("servo1: " + servo1.getPosition());
//            System.out.println ("servo2: " + servo2.getPosition());
//            System.out.println ("servo3: " + servo3.getPosition());
//        }
//
//        //Thread.sleep(3000);


            //Thread.sleep(3000);
        }
    }
}
