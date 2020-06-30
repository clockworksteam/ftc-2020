package org.firstinspires.ftc.clockworks.teleop;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Test class for the ColorSensor. The project is abandoned
 */

@TeleOp
public class TestColorSensor extends LinearOpMode {
    private ModernRoboticsI2cColorSensor sensor = null;



    @Override
    public void runOpMode() {
        sensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "sensor");

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addLine().addData("COLOr? ", sensor.readUnsignedByte(ModernRoboticsI2cColorSensor.Register.RED));
            if(sensor.readUnsignedByte(ModernRoboticsI2cColorSensor.Register.RED) != 0) {
                //do something
            }
        }

    }
}
