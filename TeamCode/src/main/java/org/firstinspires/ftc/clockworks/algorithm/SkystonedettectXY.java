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


        private static final String VUFORIA_KEY = "AR9lPRD/////AAABmZqtUVEPuktBoKK/etgDTXIdRX+D8GG+3zaVUpNCzHeiHVrpeW1TnydidgIRxf4gck6E9/IV9T0NxeAQxH5rQ5mogSeoAuebAm7IbwZJtFuOpKWvN5uK+pFG1GZSFM7Gyz97UOqurh4oYcFGyRrJcIA6GHME7NpHkP4kN8sUm0bqAsv3MkBcVE1rocnrP1TfO7IU4ViPNuBCjsONIHY9CLmyneTaJo8XkdwqJnpvoZzBywMexfn1NhIOfTcCUUNRPNigTV53MvsSsv9w1DURIvp2fCAmnkUs2f3xnw/Rk3txw8Q376UkWbAyEpwAbTitP+8VQ/lspIKYVeaxmZ9ZOdMYVAOLEHqb58ABCLZQgSeF";

        private VuforiaLocalizer vuforia;

        private TFObjectDetector tfod;

        public void init (HardwareMap hardwareMap , Telemetry telemetry) {
            this.hardwareMap = hardwareMap;
            this.telemetry = telemetry;
            initVuforia();

            if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
                initTfod();
            } else {
                telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            }

            if (tfod != null) {
                tfod.activate();
            }

        }


    public SkystonedettectXY updateTF() {
        if (tfod != null) {
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
                if (updatedRecognitions != null) {

                    last_time_updated_recognition = current_time;

                    dist_x.clear();
                    dist_y.clear();
                    type_of_stone.clear();
                    angles.clear();

                    for (Recognition recognition : updatedRecognitions) {
                        angles.add(recognition.estimateAngleToObject(AngleUnit.DEGREES));
                        type_of_stone.add(recognition.getLabel());
                        dist_x.add(Xcenter - ((recognition.getLeft() + recognition.getRight()) / 2));
                        dist_y.add(Ycenter - ((recognition.getTop() + recognition.getBottom()) / 2));
                    }
                }
            }
        }
        return this;
    }

    public SkystonedettectXY TFclose() {

            if (tfod != null) tfod.shutdown();
                return this;

}
    public List<Float> distX() {
        return dist_x;
    }

    public List<Float> distY() {
        return dist_y;
    }

    public List<String> type_stone(){
        return type_of_stone;
    }

    public List<Double> get_angles(){

        return angles;
    }

        private void initVuforia() {
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraName = hardwareMap.get(CameraName.class , "webcam");

            vuforia = ClassFactory.getInstance().createVuforia(parameters);
        }

        private void initTfod() {
            int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfodParameters.minimumConfidence = 0.85; //trb modificat
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        }

}
