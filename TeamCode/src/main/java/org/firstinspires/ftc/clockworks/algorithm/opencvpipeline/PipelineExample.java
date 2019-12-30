package org.firstinspires.ftc.clockworks.algorithm.opencvpipeline;

import android.util.Log;

import org.opencv.core.MatOfPoint;
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


    private Telemetry           telemetry               = null;
    private ColorBlobDetector   mDetector               = null;
    private File                read_file_on_device     = null;
    private PrintStream         printfile               = null;
    private Scanner             readfile                = null;
    private File                print_file_on_device    = null;
    private Scalar              mBlobColorHsv           = null;
    private Mat                 mSpectrum               = null;
    private Size                SPECTRUM_SIZE           = null;
    private Scalar              CONTOUR_COLOR           = null;


        public void init (Telemetry telemetry){
            this.telemetry = telemetry;
            mDetector = new ColorBlobDetector();


            read_file_on_device = new File("/storage/emulated/0/FTC/read.txt");
            try{
                readfile = new Scanner(read_file_on_device);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

            Mat touchedRegionRgba = new Mat(8 ,8 , CvType.CV_8UC4);

            for (int i=0; i<8; i++){
                for (int j=0; j<8; j++){
                    double aux[] = new double[4];

//                    System.out.println("intrat");

                    for (int k=0; k<4; k++) {
                        aux[k] = readfile.nextDouble();
                        System.out.println(aux[k]);
                    }

                    touchedRegionRgba.put(i, j, aux);
                }
            }

            mBlobColorHsv = new Scalar(255);
            mSpectrum = new Mat();
            SPECTRUM_SIZE = new Size(200, 64);
            CONTOUR_COLOR = new Scalar(255,0,0,255);

            Mat touchedRegionHsv = new Mat();
            Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

            // Calculate average color of touched region
            mBlobColorHsv = Core.sumElems(touchedRegionHsv);

            int pointCount = 8 * 8; //the height and the width from the other example

            for (int i = 0; i < mBlobColorHsv.val.length; i++)
                mBlobColorHsv.val[i] /= pointCount;

            mDetector.setHsvColor(mBlobColorHsv);

            Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR_EXACT);


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

            mDetector.process(input);
            List<MatOfPoint> contours = mDetector.getContours();
            Log.e(TAG, "Contours count: " + contours.size());
            Imgproc.drawContours(input, contours, -1, CONTOUR_COLOR);
        /**
         * NOTE: to see how to get data from your pipeline to your OpMode as well as how
         * to change which stage of the pipeline is rendered to the viewport when it is
         * tapped, please see {@link PipelineStageSwitchingExample}
         */

            return input;
    }
}