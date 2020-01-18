//package org.firstinspires.ftc.clockworks.algorithm.odometry;
package org.firstinspires.ftc.clockworks.algorithm.odometry;

//Measurement units: degrees, cm

/**
 * Implementation of odometry on the mathematical model of the robot
 */
public class HomeMadeOdometry implements TriOdometry  {
    private double x = 0;
    private double y = 0;
    private double teta = 0;

    private double dx = 0;
    private double dy = 0;
    private double posvect = 0;
    private boolean t = false;

    private int odleft = 0;
    private int odright = 0;
    private int odmid = 0;

    private double enc = 100;

    private double d = 10;
    private double rot = 0;

    /**
     * Returns x coordinate
     * @return
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Returns y coordinate
     * @return
     */
    @Override
    public double getY() {
        return y;
    }


    public void init(double Enc, double D, double Rot) {
        enc = Enc;
        d = D;
        rot = Rot;
    }

    /**
     * Changes the origin reference system
     * @param x the new x coordinate of the robot
     * @param y the new y coordinate of the robot
     * @param teta the new direction of the robot
     */
    public void coord_change(float x, float y, float teta) {
        this.x = x;
        this.y = y;
        this.teta = teta;
    }

    /**
     * resets the encoders on the motors
     */
    public void resetOd() {
        odleft = 0; odmid = 0; odright = 0;
    }

    /**
     * Updates the position of the robot
     * @param left the number of ticks on the left wheel
     * @param right the number of ticks on the right wheel
     * @param mid the number of ticks on the middle wheel
     */
    @Override
    public double feed(int left, int right, int mid) {
        odleft += left;
        odright += right;
        odmid += mid;

        dx = (mid/enc)*rot*Math.cos(teta);

        dy = (mid/enc)*rot*Math.sin(teta);

        x += dx;
        y += dy;

        x += ((left/enc)*rot*Math.sin(teta) + (right/enc)*rot*Math.sin(teta))/2;
        y += ((left/enc)*rot*Math.cos(teta) + (right/enc)*rot*Math.cos(teta))/2;

        //teta +=  ((right/enc)*rot -  (left/enc)*rot)/d ;
        teta += ((right - left)*rot/(enc*d));
        t = false;
        return 0;
    }


    public double getAngle() {
        posvect += Math.atan2(dy, dx);
        t = true;
        //return Math.atan2(dy, dx);
        return teta;
    }
    public double getAbsoluteAngle(int right, int left) {
        if(!t) {
            posvect += Math.atan2(dy, dx);
            t = true;
        }
        return (right-left);
    }

    public double getDistance() {
        return Math.sqrt(dy*dy+dx*dx);
    }

}

