package org.firstinspires.ftc.clockworks.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This class "VuStoneLocalizer" is our main class for detection. It uses the Vuforia augmented reality software development kit in order to tell us whether there is a Skystone in the image captured by our phone. It also contains some methods which give us some information about the Skystone position so that our robot can get in the right place in order to get? the Skystone with the gripper.
 *
 *
 */
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public class VuStoneLocalizer {
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final float stoneHeight = 2 * 25.4f;

    private static final String VUFORIA_KEY =
            "AR9lPRD/////AAABmZqtUVEPuktBoKK/etgDTXIdRX+D8GG+3zaVUpNCzHeiHVrpeW1TnydidgIRxf4gck6E9/IV9T0NxeAQxH5rQ5mogSeoAuebAm7IbwZJtFuOpKWvN5uK+pFG1GZSFM7Gyz97UOqurh4oYcFGyRrJcIA6GHME7NpHkP4kN8sUm0bqAsv3MkBcVE1rocnrP1TfO7IU4ViPNuBCjsONIHY9CLmyneTaJo8XkdwqJnpvoZzBywMexfn1NhIOfTcCUUNRPNigTV53MvsSsv9w1DURIvp2fCAmnkUs2f3xnw/Rk3txw8Q376UkWbAyEpwAbTitP+8VQ/lspIKYVeaxmZ9ZOdMYVAOLEHqb58ABCLZQgSeF";


    private VuforiaLocalizer vuforia = null;
    private VuforiaTrackables stoneTarget = null;
    private boolean visible;
    private double position;

    /**
     * Here we declare the attributes of the class.
     * The "CAMERA_CHOICE" represents a parameter that will be passed to the Vuforia engine in order to set the desired camera for the detection.
     * The float "stoneHeight" saves the real height of the Skystone so that Vuforia will be able to calculate the position of the Skystone in its augmented reality.
     * The String “VUFORIA_KEY” will save the license key (obtained for free from the website: developer.vuforia.com/license-manager) that will be used by the Vuforia instance. “VuforiaLocalizer” is the variable we will use to store our instance of the Vuforia localization engine and "VuforiaTrackables" is a parameter that will store a Skytone model the developers of the Robot Controller app had worked on.
     * "visible" and "position" are some variables that will be used lately in our detection.
     */



    public void init(HardwareMap map) {
        int cameraMonitorViewId = map.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        /**
         * This piece of code initializes the Vuforia localization engine, by creating a new object and setting its desired properties (parameters) (like the license key and the camera name that should be used in the hardwareMap).
         * The "cameraMonitorViewId" property makes a small rectangle on the phone's screen (called a camera view) which will show the image captured by the phone and the detection details.
         */




        stoneTarget = vuforia.loadTrackablesFromAsset("Skystone");
        stoneTarget.get(0).setName("Skystone");
        stoneTarget.get(0).setLocation(OpenGLMatrix
                .translation(0, 0, stoneHeight)
                .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYX, AngleUnit.DEGREES, 90, 0, -90)));
        stoneTarget.activate();

        /**
         *
         * Here we set the model of the Skystone that will be used by our detection. We then tell the engine not to rotate our Skystone coordinates, as we want to get the relative y-axis coordinate for our odometry system.
         * The last line applies the properties set for the model.
         */
    }

    public void update() {
        for (VuforiaTrackable trackable : stoneTarget)
        if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
            visible = true;
            OpenGLMatrix mat = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
            if (mat != null) {
                VectorF pos = mat.getTranslation();
                position = pos.get(0);
            } else visible = false;
        }


        /**
         * This method "update" is called repeatedly in our TeleOP in order to update the positions of the detected Skystones (if they exist).
         * All the necessary calculation are made by the Vuforia augmented reality. The variable "position" will store the first Skystone detected and the boolean "visible" will be set to true.
         * If there is no Skystone detected the boolean "visible" will be set to false.
         *
         *
         *
         *
         *
         *
         */
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * This method "isVisible" returns the availability of a Skystone.
     *
     */

    public double getPosition() {
        return position;
    }

    /**
     * This method returns the relative y-axis coordinate of the last detected Skystone.
     *
     */

    public void stop() {
        stoneTarget.deactivate();
    }

    /**
     * This is a simple method that turns off the Vuforia augmented reality engine.
     */

}
