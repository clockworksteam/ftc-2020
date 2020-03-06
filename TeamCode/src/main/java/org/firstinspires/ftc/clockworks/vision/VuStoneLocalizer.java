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

    public void init(HardwareMap map) {
        int cameraMonitorViewId = map.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        stoneTarget = vuforia.loadTrackablesFromAsset("Skystone");
        stoneTarget.get(0).setName("Skystone");
        stoneTarget.get(0).setLocation(OpenGLMatrix
                .translation(0, 0, stoneHeight)
                .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYX, AngleUnit.DEGREES, 90, 0, -90)));
        stoneTarget.activate();
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
    }

    public boolean isVisible() {
        return visible;
    }

    public double getPosition() {
        return position;
    }

    public void stop() {
        stoneTarget.deactivate();
    }
}
