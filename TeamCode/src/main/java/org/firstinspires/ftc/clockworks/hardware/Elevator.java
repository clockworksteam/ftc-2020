package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;
import org.firstinspires.ftc.robotcore.external.Telemetry;

enum MOTORS_STATE{
    FIRST,
    LAST,
    UPWARDS,
    DOWNWARDS,
    BRAKE
}

public class Elevator implements Fiber  {

    private DcMotor left_motor = null;
    private DcMotor right_motor = null;
    private Telemetry telemetry = null;

    private int skystone_height = 2000; //TODO: calculate Skystone height in motor ticks.
    private int elevator_height = 20000; //TODO: calculate elevator's maximum height in ticks.
    private int first_position;  //The TeleOP must be started with the elevator at the position in which it is collecting the Skystone.
    private int current_level = 0;

    private MOTORS_STATE motors_state;

    @Override
    public void init(InternalScheduler scheduler) {
        left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        left_motor.setDirection(DcMotorSimple.Direction.FORWARD);   //TODO: find out which motor should be set to reverse and which motor should be set in forward mode
        right_motor.setDirection(DcMotorSimple.Direction.REVERSE);

        first_position = (left_motor.getCurrentPosition() + right_motor.getCurrentPosition()) / 2;

        motors_state = MOTORS_STATE.BRAKE;

    }

    @Override
    public void tick() {
        switch (motors_state){
            case FIRST:
                left_motor.setTargetPosition(first_position);
                right_motor.setTargetPosition(first_position);
                motors_state = MOTORS_STATE.BRAKE;
                break;

            case LAST:
                left_motor.setTargetPosition(first_position + elevator_height);
                right_motor.setTargetPosition(first_position + elevator_height);
                motors_state = MOTORS_STATE.BRAKE;
                break;

            case UPWARDS:
                current_level++;
                left_motor.setTargetPosition(first_position + current_level * skystone_height);
                right_motor.setTargetPosition(first_position + current_level * skystone_height);
                motors_state = MOTORS_STATE.BRAKE;
                break;

            case DOWNWARDS:
                current_level--;
                left_motor.setTargetPosition(first_position + current_level * skystone_height);
                right_motor.setTargetPosition(first_position + current_level * skystone_height);
                motors_state = MOTORS_STATE.BRAKE;
                break;

        }

    }

    public void go_upwards() {
        if (motors_state == MOTORS_STATE.BRAKE) motors_state = MOTORS_STATE.UPWARDS;
    }

    public void go_downwards() {
        if (motors_state == MOTORS_STATE.BRAKE) motors_state = MOTORS_STATE.DOWNWARDS;
    }

    public void go_first_level() {
        if (motors_state == MOTORS_STATE.BRAKE) motors_state = MOTORS_STATE.FIRST;
    }

    public void go_last_level() {
        if (motors_state == MOTORS_STATE.BRAKE) motors_state = MOTORS_STATE.LAST;
    }

    @Override
    public void deinit() {

    }

    public void initData(HardwareMap hardwareMap, Telemetry telemetry){
        left_motor = hardwareMap.get(DcMotor.class , "lm_elevator");
        right_motor = hardwareMap.get(DcMotor.class , "rm_elevator");
        this.telemetry = telemetry;
    }

}
