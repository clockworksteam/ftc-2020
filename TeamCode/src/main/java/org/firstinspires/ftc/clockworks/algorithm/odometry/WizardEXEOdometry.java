//package org.firstinspires.ftc.clockworks.algorithm.odometry;
package org.firstinspires.ftc.clockworks.algorithm.odometry;


public class WizardEXEOdometry implements TriOdometry {


    //Position variables used for storage and calculations
    double verticalRightEncoderWheelPosition = 0, verticalLeftEncoderWheelPosition = 0, normalEncoderWheelPosition = 0, changeInRobotOrientation = 0;
    private double robotGlobalXCoordinatePosition = 0, robotGlobalYCoordinatePosition = 0, robotOrientationRadians = 0;
    private double previousVerticalRightEncoderWheelPosition = 0, previousVerticalLeftEncoderWheelPosition = 0, prevNormalEncoderWheelPosition = 0;

    //Algorithm constants
    private double robotEncoderWheelDistance;
    private double horizontalEncoderTickPerDegreeOffset;

    double COUNTS_PER_INCH = 100;

    private int verticalLeftEncoderPositionMultiplier = 1;
    private int verticalRightEncoderPositionMultiplier = 1;
    private int normalEncoderPositionMultiplier = 1;


    public void init(double Enc, double D, double Rot) {
        COUNTS_PER_INCH = Enc;
        robotEncoderWheelDistance = D;
        horizontalEncoderTickPerDegreeOffset = Enc;
    }

    /**
     * Updates the global (x, y, theta) coordinate position of the robot using the odometry encoders
     */
    @Override
    public double feed(int verticalEncoderLeft, int verticalEncoderRight, int horizontalEncoder) {
        //Get Current Positions
        verticalLeftEncoderWheelPosition = (verticalEncoderLeft * verticalLeftEncoderPositionMultiplier) * COUNTS_PER_INCH;
        verticalRightEncoderWheelPosition = (verticalEncoderRight * verticalRightEncoderPositionMultiplier) * COUNTS_PER_INCH;

        double leftChange = verticalLeftEncoderWheelPosition - previousVerticalLeftEncoderWheelPosition;
        double rightChange = verticalRightEncoderWheelPosition - previousVerticalRightEncoderWheelPosition;

        //Calculate Angle
        changeInRobotOrientation = (leftChange - rightChange) / (robotEncoderWheelDistance);
        robotOrientationRadians = ((robotOrientationRadians + changeInRobotOrientation));

        //Get the components of the motion
        normalEncoderWheelPosition = (horizontalEncoder * normalEncoderPositionMultiplier) * COUNTS_PER_INCH;
        double rawHorizontalChange = normalEncoderWheelPosition - prevNormalEncoderWheelPosition;
        double n = rawHorizontalChange - (changeInRobotOrientation * horizontalEncoderTickPerDegreeOffset);

        double p = ((rightChange + leftChange) / 2);

        //Calculate and update the position values
        robotGlobalXCoordinatePosition = robotGlobalXCoordinatePosition + (p * Math.sin(robotOrientationRadians) + n * Math.cos(robotOrientationRadians));
        robotGlobalYCoordinatePosition = robotGlobalYCoordinatePosition + (p * Math.cos(robotOrientationRadians) - n * Math.sin(robotOrientationRadians));

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
