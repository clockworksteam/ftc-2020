//package org.firstinspires.ftc.clockworks.algorithm.odometry;
package org.firstinspires.ftc.clockworks.algorithm.odometry;


import java.math.BigDecimal;
import java.math.MathContext;

public class WizardEXEOdometry implements TriOdometry {
    private static final MathContext MC = MathContext.UNLIMITED;

    //Position variables used for storage and calculations

    private BigDecimal robotGlobalXCoordinatePosition = new BigDecimal(0, MC), robotGlobalYCoordinatePosition = new BigDecimal(0, MC), robotOrientationRadians = new BigDecimal(0, MC);
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
