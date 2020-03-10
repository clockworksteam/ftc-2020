//package org.firstinspires.ftc.clockworks.algorithm.odometry;
package org.firstinspires.ftc.clockworks.algorithm.odometry;


import java.math.BigDecimal;
import java.math.MathContext;


/**
 * For this season of FTC we decided to use odometers. They are some small wheels located under our robot that are mounted in that way so they can touch the ground. They measure how many times they had rotate upon a given time, so we can use them in order to get the position of our robot in a cartesian coordinate system.
 * In this season of FTC we are using 3 odometers.
 */


public class WizardEXEOdometry implements TriOdometry {
    private static final MathContext MC = MathContext.UNLIMITED;

    //Position variables used for storage and calculations

    private BigDecimal robotGlobalXCoordinatePosition = new BigDecimal(0, MC), robotGlobalYCoordinatePosition = new BigDecimal(0, MC), robotOrientationRadians = new BigDecimal(0, MC);
    private double previousVerticalRightEncoderWheelPosition = 0, previousVerticalLeftEncoderWheelPosition = 0, prevNormalEncoderWheelPosition = 0;

    /**
     * Here we declare the variables that will be used in the odometry class. We use "BigDecimal" objects combined with the unlimited "MathContext" in order to make the phone to store infinite decimal numbers.
     * The attributes declared on the second line will store the robot position in a cartesian coordinate system.
     * The variables declared on the third line will be used in our algorithm in order to calculate the variation of the movement.
     */


    //Algorithm constants
    private final static double robotEncoderWheelDistance = 0.2;
    private final static double horizontalEncoderTickPerDegreeOffset = 0.3;
    private final static double COUNTS_PER_METER = 9473.6842105263157894736842105263;

    private final static int verticalLeftEncoderPositionMultiplier = 1;
    private final static int verticalRightEncoderPositionMultiplier = 1;
    private final static int normalEncoderPositionMultiplier = 1;
    /**
     * These are some algorithm constants that we have calculated throughout various experiments.
     */


    /**
     * Updates the global (x, y, theta) coordinate position of the robot using the odometry encoders
     */
    @Override
    public double feed(int verticalEncoderLeft, int verticalEncoderRight, int horizontalEncoder) {
        //Get Current Positions
        double verticalLeftEncoderWheelPosition = (verticalEncoderLeft * verticalLeftEncoderPositionMultiplier) * COUNTS_PER_METER;
        double verticalRightEncoderWheelPosition = (verticalEncoderRight * verticalRightEncoderPositionMultiplier) * COUNTS_PER_METER;

        double leftChange = verticalLeftEncoderWheelPosition - previousVerticalLeftEncoderWheelPosition;
        double rightChange = verticalRightEncoderWheelPosition - previousVerticalRightEncoderWheelPosition;

        // Calculate Angle
        // TODO: implement other method of orientation detection. integrating is retarded. gyro?
        double changeInRobotOrientation = (leftChange - rightChange) / (robotEncoderWheelDistance);
        robotOrientationRadians = robotOrientationRadians.add(new BigDecimal(changeInRobotOrientation, MC));

        //Get the components of the motion
        double normalEncoderWheelPosition = (horizontalEncoder * normalEncoderPositionMultiplier) * COUNTS_PER_METER;
        double rawHorizontalChange = normalEncoderWheelPosition - prevNormalEncoderWheelPosition;
        double travelSide = rawHorizontalChange - (changeInRobotOrientation * horizontalEncoderTickPerDegreeOffset);

        double travelFront = ((rightChange + leftChange) / 2);

        //Calculate and update the position values

        robotGlobalXCoordinatePosition = robotGlobalXCoordinatePosition.add(new BigDecimal(travelFront * Math.sin(robotOrientationRadians.doubleValue()) + travelSide * Math.cos(robotOrientationRadians.doubleValue()), MC));
        robotGlobalYCoordinatePosition = robotGlobalYCoordinatePosition.add(new BigDecimal(travelFront * Math.cos(robotOrientationRadians.doubleValue()) - travelSide * Math.sin(robotOrientationRadians.doubleValue()), MC));

        previousVerticalLeftEncoderWheelPosition = verticalLeftEncoderWheelPosition;
        previousVerticalRightEncoderWheelPosition = verticalRightEncoderWheelPosition;
        prevNormalEncoderWheelPosition = normalEncoderWheelPosition;

        return 0;
    }
    /**
     * The method "feed" is called repeatedly in our TeleOP in order to update the robot's position.
     * We start by calculating the distance traveled (in meters) by the left and right odometers (encoders).
     *
     *
     */



    /**
     * Returns the robot's global x coordinate
     *
     * @return global x coordinate
     */
    @Override
    public double getX() {
        return robotGlobalXCoordinatePosition.doubleValue();
    }

    /**
     * Returns the robot's global y coordinate
     *
     * @return global y coordinate
     */
    @Override
    public double getY() {
        return robotGlobalYCoordinatePosition.doubleValue();
    }

    /**
     * Returns the robot's global orientation
     *
     * @return global orientation, in degrees
     */

    @Override
    public double getOrientation() {
        return Math.toDegrees(robotOrientationRadians.doubleValue()) % 360;
    }


}
