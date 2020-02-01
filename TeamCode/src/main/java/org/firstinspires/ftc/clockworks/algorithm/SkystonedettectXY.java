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


/**
 * Comment for notebook:
 * This is a class which was made in order to get the distance (measured in pixels) and the angle from the center of the camera to the center of the nearest Skystone
 */

public class SkystonedettectXY {
        private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
        private static final String LABEL_FIRST_ELEMENT = "Stone";
        private static final String LABEL_SECOND_ELEMENT = "Skystone";
        private HardwareMap hardwareMap = null;
        private Telemetry telemetry = null;
        private float Xcenter = 320;
        private float Ycenter = 240;
        private List<Float> dist_x = new ArrayList<Float>();
        private List<Float> dist_y = new ArrayList<Float>();
        private List<String> type_of_stone = new ArrayList<String>();
        private List<Double> angles = new ArrayList<Double>();
        private long current_time, last_time = 0, last_time_updated_recognition = 0;

    /**
     * Here we declare the attributes of the class.
     * The developers of the Robot Controller app had worked on a trained model of TensorFlow AI, so we just have to tell our program which file to access in order to use the sample made by FTC's organisers.
     * We map the model and the elements contained by it using strings like "TFOD_MODEL_ASSET", "LABEL_FIRST_ELEMENT", "LABEL_SECOND_ELEMENT"
     * Starting with the fourth attribute, we declare the objects, arrays and integers used by our algorithm, and they mainly store the details of our recognition.
     * These attributes need to stay at the beginning of the class as they are to be accessed from different methods
     * "Xcenter" and "Ycenter" take the values which represents the center of the image captured by our phone.
     *
     */



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

    /**
     * Here we save the license key which will be used by the Vuforia instance.
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

    /**
     * Comment for notebook:
     * In this method we pass the "telemetry" object used in the main class and then we
     */

        public void init (HardwareMap hardwareMap , Telemetry telemetry) {
            // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
            // first.
            this.hardwareMap = hardwareMap;
            this.telemetry = telemetry;
            initVuforia();



            /**
             * Here we verify if we the phone is capable of handling the TensorFlow program
             */

            if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
                initTfod();
            } else {
                telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            }

            /**
             * Activate TensorFlow Object Detection before we wait for the start command.
             * We do it here in order to get TensorFlow and camera ready before the autonomous starts.
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

    /**
     * This method is called repeatedly in our algorithm in order to update the arrays that store the position of the Skystones in front of the robot.
     *
     */


    public SkystonedettectXY updateTF() {
        if (tfod != null) {             //Here we verify that TensorFlow is running correctly
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
            /**
             * Here we set a timer which deletes the content of the arrays if TensorFlow is incapable of finding a Stone or a Skystone for 0.8 seconds, as this time makes us sure that there is no type of stone in front of our robot
             */


            /**
             * Another timer which helps TensorFlow not to overload, as processing 12 frames per second should be enough for a reliable detection.
             */
            if (current_time - last_time > 80) {

                last_time = current_time;

                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
//            if (updatedRecognitions == null) System.out.println("yes");
//            else System.out.println("no");
                if (updatedRecognitions != null) {         //This piece of code is executed only if TensorFlow finds out that the objects recognized in the last image are located at another coordinates in the frame.

                    last_time_updated_recognition = current_time;

                    dist_x.clear();
                    dist_y.clear();
                    type_of_stone.clear();            //Here we clear the arrays in order to update them with the new values of the recognition
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

                        /**
                         * Here we store the details of the recognition calculated by TensorFlow program, computing also the 2-axis projection of the distance from the center to the nearest stones
                         */



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
         * Initialize the Vuforia localization engine, by creating a new object and setting its desired properties (like the license key and the camera name that should be used in the hardwareMap).
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
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);   //This is used to make a small rectangle on the phone's screen (called a camera view) which will show the image captured by the webcam.
            tfodParameters.minimumConfidence = 0.85; //trb modificat
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);   //Here we set the minimum confidence which TensorFlow should have for an object in order to return it as a type of recognition and also we pass strings containing the name of the model and its elements, as TensorFlow should for what to look in the asset created by the FTC's developers
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        }



}
