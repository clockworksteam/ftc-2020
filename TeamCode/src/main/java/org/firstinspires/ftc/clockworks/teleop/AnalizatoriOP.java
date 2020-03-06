package org.firstinspires.ftc.clockworks.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.algorithm.motion.MotionCommander;
import org.firstinspires.ftc.clockworks.algorithm.odometry.WizardEXEOdometry;
import org.firstinspires.ftc.clockworks.control.Odometry;
import org.firstinspires.ftc.clockworks.control.PositionController;
import org.firstinspires.ftc.clockworks.hardware.BackServos;
import org.firstinspires.ftc.clockworks.hardware.GamepadInput;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.Intake;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;
import org.firstinspires.ftc.clockworks.hardware.ServoGripper;
import org.firstinspires.ftc.clockworks.scheduler.ExternalScheduler;
import org.firstinspires.ftc.clockworks.scheduler.OnDemandScheduler;
import org.firstinspires.ftc.clockworks.vision.VuStoneLocalizer;

@TeleOp (name = "AnalizatoriiOP", group = "Drive")
public class AnalizatoriOP extends LinearOpMode {
    private MecanumDriver mecanum = new MecanumDriver();
    private Odometry odometry = new Odometry(new WizardEXEOdometry(), hardwareMap, "left", "right", "mid");
    private Intake intake = new Intake();
    private BackServos backServos = new BackServos();
    private GamepadInput gamepadInput = new GamepadInput();
    private IMUSensor imuSensor = new IMUSensor();
    private ServoGripper servoGripper = new ServoGripper();
    private ExternalScheduler scheduler = OnDemandScheduler.create();
    private VuStoneLocalizer vustone = new VuStoneLocalizer();
    private PositionController positionController = new PositionController();
    private MotionCommander motionCommander = new MotionCommander(positionController, odometry);



    @Override
    public void runOpMode() {
        mecanum.init(telemetry, hardwareMap, "motor");
        intake.initData(hardwareMap, "intake");
        backServos.initData(hardwareMap, "servo");
        gamepadInput.init(gamepad1, gamepadInput);
        imuSensor.init(telemetry, hardwareMap, "imusensor");
        servoGripper.initData(hardwareMap, telemetry);
        vustone.init(hardwareMap);
        positionController.initData(telemetry, mecanum, imuSensor);

        scheduler.register(backServos);
        scheduler.register(intake);
        scheduler.register(motionCommander);
        scheduler.register(positionController);
        scheduler.register(servoGripper);
        scheduler.register(mecanum);

    }


}
