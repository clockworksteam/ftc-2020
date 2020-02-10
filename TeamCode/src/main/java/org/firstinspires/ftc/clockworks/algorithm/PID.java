package org.firstinspires.ftc.clockworks.algorithm;

/**
 * PID implementation
 */
public class PID {
    final double kp, ki, kd; // Constants which we choose experimentally
    double lastError;
    double lastTime;
    double integral;
    double target;


    /**
     * Initialization
     * @param kp constant for the proportional
     * @param ki constant for the integral
     * @param kd constant for the derivative
     */
    public PID(double kp, double ki, double kd) {
        this.ki = ki;
        this.kp = kp;
        this.kd = kd;
        target = 0;
        reset(0);
    }

    /**
     * setting the target
     * @param target the target for the PID controller
     */
    public void setTarget(double target) {
        this.target = target;
    }

    /**
     * The derivative measures the error in the elapsed time
     * The integral approximates the area of error (how far the robot has deviated)
     * @param value input value
     * @param time the elapsed time
     * @return returning the correction
     */
    public double feed(double value, double time) {
        double error = target - value;
        double derivative = (error - lastError) / (time - lastTime);
        integral += (error * time);
        lastError = error;
        lastTime = time;
        return error * kp + integral * ki + derivative * kd; //The equation
    }

    public void reset(double time) {
        lastError = 0;
        lastTime = time;
        integral = 0;
    }
}