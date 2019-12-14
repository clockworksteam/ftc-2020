package org.firstinspires.ftc.clockworks.algorithm;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.sql.SQLOutput;
import java.util.List;
import java.util.ArrayList;

/**
 * This class is based on the ConceptTensorFlowObjectDetection.java class from the FTC examples
 * Firstly, it finds camera's center position based on phones mode: landscape or portrait
 * Then it initializes Vuforia and loops through the detected skystones
 * Using the distX and distY methods, the program loops through the positions of the skystones
 * and calculates the distance from the center of the stones to the center of the screen on the x and y coordinates
 * they then return an Arraylist with the distances
 */

public class SkystonedettectXY {
        private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
        private static final String LABEL_FIRST_ELEMENT = "Stone";
        private static final String LABEL_SECOND_ELEMENT = "Skystone";
        private HardwareMap hardwareMap = null;
        private Telemetry telemetry = null;
        private  float Xcenter = 320;
        private float Ycenter = 240;
        private List<Float> dist_x = new ArrayList<Float>();
        private List<Float> dist_y = new ArrayList<Float>();
        private List<String> type_of_stone = new ArrayList<String>();
        private List<Double> angles = new ArrayList<Double>();
        private long current_time, last_time = 0, last_time_updated_recognition = 0;


    /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code on the next line, between the double quotes.
         */
        private static final String VUFORIA_KEY = "AR9lPRD/////AAABmZqtUVEPuktBoKK/etgDTXIdRX+D8GG+3zaVUpNCzHeiHVrpeW1TnydidgIRxf4gck6E9/IV9T0NxeAQxH5rQ5mogSeoAuebAm7IbwZJtFuOpKWvN5uK+pFG1GZSFM7Gyz97UOqurh4oYcFGyRrJcIA6GHME7NpHkP4kN8sUm0bqAsv3MkBcVE1rocnrP1TfO7IU4ViPNuBCjsONIHY9CLmyneTaJo8XkdwqJnpvoZzBywMexfn1NhIOfTcCUUNRPNigTV53MvsSsv9w1DURIvp2fCAmnkUs2f3xnw/Rk3txw8Q376UkWbAyEpwAbTitP+8VQ/lspIKYVeaxmZ9ZOdMYVAOLEHqb58ABCLZQgSeF";

        /**
         * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
         * localization engine.
         */
        private VuforiaLocalizer vuforia;

        /**
         * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
         * Detection engine.
         */
        private TFObjectDetector tfod;

    /**
     *Init method description:
     * @param hardwareMap
     * @param telemetry is used to display the information on the phone
     */
        public void init (HardwareMap hardwareMap , Telemetry telemetry) {
            // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
            // first.
            this.hardwareMap = hardwareMap;
            this.telemetry = telemetry;
            initVuforia();

            if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
                initTfod();
            } else {
                telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            }

            /**
             * Activate TensorFlow Object Detection before we wait for the start command.
             * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
             **/
            if (tfod != null) {
                tfod.activate();
            }

            /** Wait for the game to begin */
//            telemetry.addData(">", "TensorFlow loaded");
//            telemetry.update();
        }

    /**
     * unpdateTF method description:
     * @return it returns the object in order to chain multiple commands
     * The method tests if new information is available and then it updates the updateRecognition list
     * This lists stores the objects detected
     * dist_x and dist_y lists are used to store the length from the x and y coordinate of the center of the object to the coordinates of the center
     */
    public SkystonedettectXY updateTF() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.




           // System.out.println("intrat UPDATETF");  //Am I drunk ?
                    //Simply uncommenting this line makes the loop not to enter the next block



            current_time = System.currentTimeMillis();

            if (current_time - last_time_updated_recognition > 800){
                dist_x.clear();
                dist_y.clear();
                type_of_stone.clear();
                angles.clear();

            }

            if (current_time - last_time > 80) {

                last_time = current_time;

                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
//            if (updatedRecognitions == null) System.out.println("yes");
//            else System.out.println("no");
                if (updatedRecognitions != null) {

                    last_time_updated_recognition = current_time;

                    dist_x.clear();
                    dist_y.clear();
                    type_of_stone.clear();
                    angles.clear();

                //    dist_x.clear();
                //    dist_y.clear();

                //System.out.println("Intrat-updatedRecognitions");

                // step through the list of recognitions and display boundary info
                    for (Recognition recognition : updatedRecognitions) {
                        // System.out.println("e");
                        angles.add(recognition.estimateAngleToObject(AngleUnit.DEGREES));
                        type_of_stone.add(recognition.getLabel());
                        dist_x.add(Xcenter - ((recognition.getLeft() + recognition.getRight()) / 2));
                        dist_y.add(Ycenter - ((recognition.getTop() + recognition.getBottom()) / 2));


                   /*System.out.println("intrat");
                    if(!angles.isEmpty()) System.out.println("nu e empty");
                    else System.out.println("E empty");*/
                    }
                }
            }
        }
        return this;
    }

    /**
     * TFclose description:
     * @return it returns the object in order to chain multiple commands
     * It turns off the TensorFlow object
     */
    public SkystonedettectXY TFclose() {

            if (tfod != null) tfod.shutdown();
                return this;

}

    /**
     * distX description:
     * @return returns the dist_x list with the distance from x coordiante of the stones to the center
     */
    public List<Float> distX() {
        return dist_x;
    }

    /**
     * distY description:
     * @return returns the dist_y list with the distance from y coordiante of the stones to the center
     */
    public List<Float> distY() {
        return dist_y;
    }

    public List<String> type_stone(){
        return type_of_stone;
    }

    public List<Double> get_angles(){

//        if(!angles.isEmpty()) System.out.println("nu e empty");
//        else System.out.println("E empty");

        return angles;
    }

        /**
         * Initialize the Vuforia localization engine.
         */
        private void initVuforia() {
            /*
             * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
             */
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraName = hardwareMap.get(CameraName.class , "webcam");

            //  Instantiate the Vuforia engine
            vuforia = ClassFactory.getInstance().createVuforia(parameters);

            // Loading trackables is not necessary for the TensorFlow Object Detection engine.
        }

        /**
         * Initialize the TensorFlow Object Detection engine.
         */
        private void initTfod() {
            int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfodParameters.minimumConfidence = 0.85; //trb modificat
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        }



}
