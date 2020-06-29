package org.firstinspires.ftc.clockworks.control;


import org.firstinspires.ftc.clockworks.scheduler.Fiber;
import org.firstinspires.ftc.clockworks.scheduler.InternalScheduler;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.clockworks.hardware.IMUSensor;
import org.firstinspires.ftc.clockworks.hardware.MecanumDriver;
import org.firstinspires.ftc.clockworks.algorithm.AngularPID;

/**
 * PositionController class for implementing all the algorithms and classes designed for controlling the Mecanum Wheels (PID, IMUSensor and MecanumDriver).
 */
public class PositionController implements Fiber {


    private MecanumDriver mecanumDriver = new MecanumDriver();
    private IMUSensor imuSensor = new IMUSensor();
    private AngularPID pid = new AngularPID(0.015, 0.000000, 0.0);
    private Telemetry telemetry = null;
    private volatile double angle = 0, power = 0;
    private volatile double blindRotatePower = 0;
    private volatile boolean isBlindRotating = false;


    @Override
    public void init(InternalScheduler scheduler) {

    }

    @Override
    public void tick() {
        double currentHeading = imuSensor.getHeading();
        double dir = angle - currentHeading;
        double rot;
        if (isBlindRotating) {
            rot = blindRotatePower;
        } else {
            rot = pid.feed(currentHeading, System.currentTimeMillis() / 1000.0);
        }
        mecanumDriver.drive(power, dir, rot);
    }

    @Override
    public void deinit() {

    }

    /**
     * Initializes the PositionController
     *
     * @param telemetry     the telemetry logger
     * @param mecanumDriver an instance of the MecanumDriver class which is used to control the motors in the TeleOP.
     * @param imuSensor     an instance of the IMUSensor class which is used for getting the angles from the gyro sensor.
     */
    public void initData(Telemetry telemetry, MecanumDriver mecanumDriver, IMUSensor imuSensor) {
        this.telemetry = telemetry;
        this.mecanumDriver = mecanumDriver;
        this.imuSensor = imuSensor;
    }

    /**
     * This method makes the robot rotate around its own axe wit no precision(we don't set any target or distance
     * this is used in TELEOP
     *
     * @param power the power set to the motors in order to rotate
     * @return self reference (this). Useful in chaining methods
     */
    public PositionController blindRotate(double power) {
        blindRotatePower = power;
        isBlindRotating = true;
        return this;
    }

    /**
     * This method should be called when we want to use PID(rotate around its own axe)-precise
     * it sets the heading to which we want the robot to go to(absolute heading)
     *
     * @param heading the heading to which we want the robot to go to
     * @return self reference (this). Useful in chaining methods
     */
    public PositionController setHeading(double heading) {
        pid.setTarget(heading);
        isBlindRotating = false;
        return this;
    }

    /**
     * This method should be called in order to find out if we are blind rotating or not
     * it sets the heading to which we want the robot to go to
     *
     * @return self reference (this). Useful in chaining methods
     */
    public boolean isBlindRotating() {
        return isBlindRotating;
    }


    /**
     * used when we want the robot to go in a certain direction(absolute,not relative to the current orientation of the robot)
     * it sets the heading to which we want the robot to go to
     *
     * @return self reference (this). Useful in chaining methods
     */
    public PositionController setDirection(double angle, double power) {
        this.angle = angle;
        this.power = power;
        return this;
    }
}




/*
Prieteni ,avem 5 cazuri:

1.  Vrem ca robotul sa nu isi schimbe orientarea si sa mearga intr.o directie alfa fata de Nord
    -dam putere si (probabil) ca apelam fuctia asta pt o perioda a de timp t
    -setDirection()

2. sa mergem ca la 1.,doar ca sa mearga o anumita distanta in direactia data??????(inca nu avem fucntie)

3.  Te rotesti in jurul axei pana la un anumit punct fix(un direction dat la parametrii-absolut)
    -setHeading()

3.  Sa se roteasca in jurul axei,dar fara un targeted direction
    -BlindRotate() -dam o anumita putere intr.un interval de timp--folosit in TELEOP

4.  Spirala --blindRotate() + settDirection() (raza variaza in functie de timp)

5.  sa mearga drept catre un punct
    -setDirection(currentDirection ,power) -intr.un interval de timp
 */