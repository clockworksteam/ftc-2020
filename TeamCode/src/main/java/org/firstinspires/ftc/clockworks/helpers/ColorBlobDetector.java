package org.firstinspires.ftc.clockworks.helpers;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


/**
 * Only for test
 */

public class ColorBlobDetector {
    // Lower and Upper bounds for range checking in HSV color space
    private Scalar                      mLowerBound     = null;
    private Scalar                      mUpperBound     = null;
    // Minimum contour area in percent for contours filtering
    private static double               mMinContourArea;
    // Color radius for range checking in HSV color space
    private Scalar                      mColorRadius    = null;
    private Mat                         mSpectrum       = null;
    private List<MatOfPoint>            mContours       = null;

    // Cache
    private Mat                         mPyrDownMat     = null;
    private Mat                         mHsvMat         = null;
    private Mat                         mMask           = null;
    private Mat                         mDilatedMask    = null;
    private Mat                         mHierarchy      = null;


    private Telemetry                   telemetry       = null;

    double totalArea;
    double maxArea;

    public void init(Telemetry telemetry){
        this.telemetry          = telemetry;

        mLowerBound             = new Scalar(0);
        mUpperBound             = new Scalar(0);
        mMinContourArea         = 0.1;
        mColorRadius            = new Scalar(25,50,50,0);
        mSpectrum               = new Mat();
        mContours               = new ArrayList<MatOfPoint>();


        mPyrDownMat             = new Mat();
        mHsvMat                 = new Mat();
        mMask                   = new Mat();
        mDilatedMask            = new Mat();
        mHierarchy              = new Mat();

    }




    public void setColorRadius(Scalar radius) {
        mColorRadius = radius;
    }

    public void setHsvColor(Scalar hsvColor) {
        double minH = (hsvColor.val[0] >= mColorRadius.val[0]) ? hsvColor.val[0]-mColorRadius.val[0] : 0;
        double maxH = (hsvColor.val[0]+mColorRadius.val[0] <= 255) ? hsvColor.val[0]+mColorRadius.val[0] : 255;

        mLowerBound.val[0] = minH;
        mUpperBound.val[0] = maxH;

        mLowerBound.val[1] = hsvColor.val[1] - mColorRadius.val[1];
        mUpperBound.val[1] = hsvColor.val[1] + mColorRadius.val[1];

        mLowerBound.val[2] = hsvColor.val[2] - mColorRadius.val[2];
        mUpperBound.val[2] = hsvColor.val[2] + mColorRadius.val[2];

        mLowerBound.val[3] = 0;
        mUpperBound.val[3] = 255;

        Mat spectrumHsv = new Mat(1, (int)(maxH-minH), CvType.CV_8UC3);

        for (int j = 0; j < maxH-minH; j++) {
            byte[] tmp = {(byte)(minH+j), (byte)255, (byte)255};
            spectrumHsv.put(0, j, tmp);
        }

        Imgproc.cvtColor(spectrumHsv, mSpectrum, Imgproc.COLOR_HSV2RGB_FULL, 4);
    }

    public Mat getSpectrum() {
        return mSpectrum;
    }

    public void setMinContourArea(double area) {
        mMinContourArea = area;
    }

    public void process(Mat rgbaImage) {
        Imgproc.pyrDown(rgbaImage, mPyrDownMat);
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);

        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask);
        Imgproc.dilate(mMask, mDilatedMask, new Mat());

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find max contour area
        maxArea = 0;
        totalArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            totalArea += area;
            if (area > maxArea)
                maxArea = area;
        }

        // Filter contours by area and resize to fit the original image size
        mContours.clear();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            if (Imgproc.contourArea(contour) > mMinContourArea*maxArea) {
                Core.multiply(contour, new Scalar(4,4), contour);
                mContours.add(contour);
            }
        }
    }

    public List<MatOfPoint> getContours() {
        return mContours;
    }

    public double get_total_area(){
        return totalArea;
    }


}