//package org.firstinspires.ftc.clockworks.algorithm.odometry;
package org.firstinspires.ftc.clockworks.algorithm.odometry;


public class WizardEXEOdometry implements TriOdometry {


    //Position variables used for storage and calculations
    private double verticalRightEncoderWheelPosition = 0, verticalLeftEncoderWheelPosition = 0, normalEncoderWheelPosition = 0, changeInRobotOrientation = 0;
    private double robotGlobalXCoordinatePosition = 0, robotGlobalYCoordinatePosition = 0, robotOrientationRadians = 0;
    private double previousVerticalRightEncoderWheelPosition = 0, previousVerticalLeftEncoderWheelPosition = 0, prevNormalEncoderWheelPosition = 0;

    //Algorithm constants
    private final static double robotEncoderWheelDistance = 0.2;
    private final static double horizontalEncoderTickPerDegreeOffset = 0.3;
    private final static double COUNTS_PER_METER = 9473.6842105263157894736842105263;

    private final static int verticalLeftEncoderPositionMultiplier = 1;
    private final static int verticalRightEncoderPositionMultiplier = 1;
    private final static int normalEncoderPositionMultiplier = 1;

    /**
     * Updates the global (x, y, theta) coordinate position of the robot using the odometry encoders
     */
    @Override
    public double feed(int verticalEncoderLeft, int verticalEncoderRight, int horizontalEncoder) {
        //Get Current Positions
        verticalLeftEncoderWheelPosition = (verticalEncoderLeft * verticalLeftEncoderPositionMultiplier) * COUNTS_PER_METER;
        verticalRightEncoderWheelPosition = (verticalEncoderRight * verticalRightEncoderPositionMultiplier) * COUNTS_PER_METER;

        double leftChange = verticalLeftEncoderWheelPosition - previousVerticalLeftEncoderWheelPosition;
        double rightChange = verticalRightEncoderWheelPosition - previousVerticalRightEncoderWheelPosition;

        // Calculate Angle
        // TODO: implement other method of orientation detection. integrating is retarded. gyro?
        changeInRobotOrientation = (leftChange - rightChange) / (robotEncoderWheelDistance);
        robotOrientationRadians = robotOrientationRadians + changeInRobotOrientation;

        //Get the components of the motion
        normalEncoderWheelPosition = (horizontalEncoder * normalEncoderPositionMultiplier) * COUNTS_PER_METER;
        double rawHorizontalChange = normalEncoderWheelPosition - prevNormalEncoderWheelPosition;
        double travelSide = rawHorizontalChange - (changeInRobotOrientation * horizontalEncoderTickPerDegreeOffset);

        double travelFront = ((rightChange + leftChange) / 2);

        //Calculate and update the position values
        robotGlobalXCoordinatePosition += travelFront * Math.sin(robotOrientationRadians) + travelSide * Math.cos(robotOrientationRadians);
        robotGlobalYCoordinatePosition += travelFront * Math.cos(robotOrientationRadians) - travelSide * Math.sin(robotOrientationRadians);

        previousVerticalLeftEncoderWheelPosition = verticalLeftEncoderWheelPosition;
        previousVerticalRightEncoderWheelPosition = verticalRightEncoderWheelPosition;
        prevNormalEncoderWheelPosition = normalEncoderWheelPosition;

        return 0;
    }

    /**
     * Returns the robot's global x coordinate
     *
     * @return global x coordinate
     */
    @Override
    public double getX() {
        return robotGlobalXCoordinatePosition;
    }

    /**
     * Returns the robot's global y coordinate
     *
     * @return global y coordinate
     */
    @Override
    public double getY() {
        return robotGlobalYCoordinatePosition;
    }

    /**
     * Returns the robot's global orientation
     *
     * @return global orientation, in degrees
     */
    public double returnOrientation() {
        return Math.toDegrees(robotOrientationRadians) % 360;
    }


}
