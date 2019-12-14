package org.firstinspires.ftc.clockworks.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.Gripping;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.time.Clock;

@Disabled
//de testat
@TeleOp(name = "GripperTest", group = "Drive")
public class GripperTest extends LinearOpMode{
    private GamepadInput controller = new GamepadInput();
    private Gripping gripper = new Gripping();
    private HardwareMap hardwareMap = null;
    private int bState = 0;
    private int yState = 0;
    private int xState = 0;
    ElapsedTime runtime = new ElapsedTime();
    double time_passed;
    boolean gripper_back_state = false; //it is supposed that the gripper is closed (and that the angle is0 degrees)when we start the programme
    boolean gripper_front_state = false;

    @Override
    public void runOpMode() {
        gripper.init(hardwareMap, "Gripper");
        controller.init(gamepad1);
        waitForStart();
        runtime.reset();
        while(opModeIsActive()) {
            controller.readDevice();


            if(controller.was_pressed('B') && (gripper_back_state==false)) {
                gripper.openBack();
                gripper_back_state=true; //it is opened
            }

            if(controller.was_pressed('B') && (gripper_back_state==true)) {
                gripper.closeBack();
                gripper_back_state=false; //it is closed
            }

            if(controller.was_pressed('Y') && (gripper_front_state==false)) {
                gripper.openFront();
                gripper_front_state=true; //it is opened
            }

            if(controller.was_pressed('Y') && (gripper_front_state==true)) {
                gripper.closeFront();
                gripper_front_state=false; //it is closed
            }



            if(gamepad1.x && xState == 0) {
               // gripper.p
                xState = 2;
            }



            if (gamepad1.right_trigger > 0){}
               // gripper.paralel();
        }

    }}