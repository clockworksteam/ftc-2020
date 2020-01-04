package org.firstinspires.ftc.clockworks.algorithm.opencvpipeline;

import android.util.Log;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


import org.firstinspires.ftc.clockworks.helpers.ColorBlobDetector;
import org.firstinspires.ftc.clockworks.helpers.Easymethods;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.io.PrintStream;

/*
 * An example image processing pipeline to be run upon receipt of each frame from the camera.
 * Note that the processFrame() method is called serially from the frame worker thread -
 * that is, a new camera frame will not come in while you're still processing a previous one.
 * In other words, the processFrame() method will never be called multiple times simultaneously.
 *
 * However, the rendering of your processed image to the viewport is done in parallel to the
 * frame worker thread. That is, the amount of time it takes to render the image to the
 * viewport does NOT impact the amount of frames per second that your pipeline can process.
 *
 * IMPORTANT NOTE: this pipeline is NOT invoked on your OpMode thread. It is invoked on the
 * frame worker thread. This should not be a problem in the vast majority of cases. However,
 * if you're doing something weird where you do need it synchronized with your OpMode thread,
 * then you will need to account for that accordingly.
 */


public class PipelineExample extends OpenCvPipeline {
    /*
     * NOTE: if you wish to use additional Mat objects in your processing pipeline, it is
     * highly recommended to declare them here as instance variables and re-use them for
     * each invocation of processFrame(), rather than declaring them as new local variables
     * each time through processFrame(). This removes the danger of causing a memory leak
     * by forgetting to call mat.release(), and it also reduces memory pressure by not
     * constantly allocating and freeing large chunks of memory.
     */
    private static final String TAG                     = "OCVSample::Activity";

    private Rect                rect                    = null;
    private Telemetry           telemetry               = null;
    private ColorBlobDetector   mDetector               = null;
    private File                black_file_on_device    = null;
    private PrintStream         printfile               = null;
    private Scanner             blackfile               = null;
    private Scanner             yellowfile              = null;
    private File                yellow_file_on_device   = null;
    private Scalar              mBlobColorHsv           = null;
    private Mat                 mSpectrum               = null;
    private Size                SPECTRUM_SIZE           = null;
    private Scalar              CONTOUR_COLOR           = null;


        public void init (Telemetry telemetry, ColorBlobDetector colorBlobDetector){
            this.telemetry              = telemetry;
            mDetector                   = colorBlobDetector;
            mBlobColorHsv               = new Scalar(255);
            mSpectrum                   = new Mat();
            SPECTRUM_SIZE               = new Size(200, 64);
            CONTOUR_COLOR               = new Scalar(255,0,0,255);

            rect                        = new Rect();
            rect.x                      = 32;                    //Since we know the resolution used by the camera, we can hardcode a rect in which we expect to capture the black side of the Skystone
            rect.y                      = 24;
            rect.width                  = 256;
            rect.height                 = 192;

            Mat touchedRegionRgba       = new Mat(8 ,8 , CvType.CV_8UC4);
            Mat touchedRegionHsv        = new Mat();
            int pointCount              = 8 * 8;                //the height and the width used to calculate the average color in the other example

            black_file_on_device        = new File("/storage/emulated/0/FTC/black.txt");
            try{
                blackfile = new Scanner(black_file_on_device);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

            yellow_file_on_device       = new File("/storage/emulated/0/FTC/yellow.txt");
            try{
                yellowfile = new Scanner(yellow_file_on_device);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }


            for (int i=0; i<8; i++){                            //the height and the width used to calculate the average color of touched region in the other example
                for (int j=0; j<8; j++){

                    double aux[] = new double[4];
                    for (int k=0; k<4; k++) {
                        aux[k] = blackfile.nextDouble();
                    }
                    touchedRegionRgba.put(i, j, aux);
                }
            }


            Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);


            // Calculate average color of touched region
            mBlobColorHsv = Core.sumElems(touchedRegionHsv);
            for (int i = 0; i < 4; i++)                     //because a Scalar object is a 4 element vector
                mBlobColorHsv.val[i] /= pointCount;
            mDetector.setHsvColor(mBlobColorHsv);

            touchedRegionRgba.release();
            touchedRegionHsv.release();

        }

         @Override
        public Mat processFrame(Mat input)
        {
        /*
         * IMPORTANT NOTE: the input Mat that is passed in as a parameter to this method
         * will only dereference to the same image for the duration of this particular
         * invocation of this method. That is, if for some reason you'd like to save a copy
         * of this particular frame for later use, you will need to either clone it or copy
         * it to another Mat.
         */

            Mat rectangle = input.submat(rect);
            mDetector.process(rectangle);
            List<MatOfPoint> contours = mDetector.getContours();
            Log.e(TAG, "Contours count: " + contours.size());
            Imgproc.drawContours(rectangle, contours, -1, CONTOUR_COLOR);
        /**
         * NOTE: to see how to get data from your pipeline to your OpMode as well as how
         * to change which stage of the pipeline is rendered to the viewport when it is
         * tapped, please see {@link PipelineStageSwitchingExample}
         */

            Imgproc.rectangle(
                    input,
                    rect,
                    new Scalar(0, 255, 0), 4);


            return input;
    }
}