package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;
import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This is a class that is used to control our gripper designed for catching stones.
 */

public class ServoGripper {

    Servo servo0 = null;
    Servo servo1 = null;
    Servo servo2 = null;
    Servo servo3 = null;
    private boolean closed = false;
    private boolean current = false;

    /**
     * Here we declare our four servo motors that are mounted on the gripper in order to move it.
     * The booleans "closed" and "current" are used in our algorithms in order to get the current state and to set the current state.
     *
     */


    public void tick() {
        if (current != closed) {
            current = closed;
            if (current) {
                servo0.setPosition(0.5);
                servo1.setPosition(0.5);
                servo2.setPosition(0.5);
                servo3.setPosition(0.5);
            } else {
                servo0.setPosition(0.35);
                servo1.setPosition(0.65);
                servo2.setPosition(0.35);
                servo3.setPosition(0.65);
            }
        }
    }

    /**
     * This method "tick" is used to make the gripper execute the two movements: opening and closing.
     * We use 2 "if" conditions in order to make the arbitrary move of the current state.
     * For example: if the gripper is opened , the code will set all the servos to the position 0.5 (which is the position in which the gripper is closed).
     * Otherwise the servos will be set to the position (0.35) respectively (0.65) , as this is the position in which the gripper is opened (There are different values because some motors are in opposite positions).
     *
     *
     */


    public void init(HardwareMap hardwareMap){
        servo0 = hardwareMap.get(Servo.class , "servo0");
        servo1 = hardwareMap.get(Servo.class , "servo1");
        servo2 = hardwareMap.get(Servo.class , "servo2");
        servo3 = hardwareMap.get(Servo.class , "servo3");
    }

    /**
     * This is the init class.
     * Here is made the connection between the code and the hardware (servos).
     */

    public void open_gripper () {
        closed = false;


    }

    /**
     * This method is used to make the gripper to open by setting the boolean "closed" to false.
     */


    public void close_gripper () {
        closed = true;


    }

    /**
     * This method is used to make the gripper to close by setting the boolean "closed" to true.
     */

}
