package org.firstinspires.ftc.clockworks.teleop;

import org.firstinspires.ftc.clockworks.algorithm.SkystonedettectXY;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.clockworks.hardware.*;
import org.firstinspires.ftc.clockworks.helpers.AngleHelper;
import org.firstinspires.ftc.clockworks.algorithm.*;
import org.firstinspires.ftc.clockworks.control.*;
import org.firstinspires.ftc.clockworks.helpers.AngleHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.firstinspires.ftc.clockworks.helpers.AngleHelper.norm;

/**
 * This TeleOp code tests the class SkystonedettectXY.java in order to see if the code works
 * It acceses the distX and distY methods from the above mentioned class and shows using telemetry on the phone screen the distance form the ceneter of each
 * skystone to the center of the image on the x and y coordinates
 * robotul gaseste skystone.ul ,se roteste pana ajunge cu fata la centrul lui ,dupa(in al 3lea while) ,merge la el
 */
@TeleOp(name = "TeleOPTestforTensorFlow_B_", group = "Drive")
public class TeleOPTestforTensorFlow_B_ extends LinearOpMode {
    private MecanumDriver mecanumDriver = new MecanumDriver();
    private SkystonedettectXY skystonedetectXY = new SkystonedettectXY();
    private PositionController positionController = new PositionController();
    private IMUSensor imuSensor = new IMUSensor();

    private List<Float> xtothecenter = new ArrayList<Float>();
    private List<Float> ytothecenter = new ArrayList<Float>();

    private List<String> labels = new ArrayList<String>();
    private List<Double> angles = new ArrayList<Double>();
    private ElapsedTime runtime = new ElapsedTime();
    private int i;
    DecimalFormat decimalFormat = new DecimalFormat(".##");
    private int index;//when we want to findout the indexof the skystone in the array
    double angle_to_show;
    static boolean found;

    private double heading1;
    private double angle_to_skystone;

    boolean skystone_found = false;

    /**
     * The runOpMode initialises a SkystonedettectXY object
     * It then takes the lists with the distance from the objects to the center of the image on the two coordinates
     * Using telemetry, it displays these values on the screen
     */
    @Override
    public void runOpMode() {
        skystonedetectXY.init(hardwareMap, telemetry);
        mecanumDriver.init(telemetry, hardwareMap, "ok");
        imuSensor.init(telemetry, hardwareMap, "imu");
        positionController.init(telemetry, mecanumDriver, imuSensor);

        waitForStart();
        runtime.reset();

        imuSensor.readDevice();

        positionController.setDirection(-90, 0.2);
        positionController.update();
        found = false;
        while (opModeIsActive() && found == false) {

            skystonedetectXY.updateTF();
            labels = skystonedetectXY.type_stone();
            angles =skystonedetectXY.get_angles(); // ??
            imuSensor.readDevice();
            if (!labels.isEmpty()) {
                imuSensor.readDevice();
                positionController.setDirection(-90, 0.09);

                for (String stone : labels) {
                    if (stone.contentEquals("Skystone")) {
                        positionController.setDirection(-90, 0.03);
                        xtothecenter = skystonedetectXY.distX();
                        index = stone.indexOf(stone);
                        found=true;
//                        if (Math.abs(xtothecenter.get(index)) < 30) {
//                            found = true;
//                        }



                    }

                }
            }


        }

        found=false;

        while(opModeIsActive()&& found==false) {
            skystonedetectXY.updateTF();
            imuSensor.readDevice();

            angle_to_skystone=angles.get(index);
            xtothecenter = skystonedetectXY.distX();
            ytothecenter = skystonedetectXY.distY();

            if(angle_to_skystone>10) //as putea pune in conditue ca xtothecenter sa fie mai mare ca 10?
            {
                positionController.blindRotate(0.02);
                positionController.setDirection(0,0.01); //nu inteleg dc trebuie sa setez de 2 ori power
                positionController.update();

                telemetry.addData("angle_to_skystone ",angle_to_skystone);
            }
            else
            {
                found=true;
                telemetry.addData("AM ajuns la skystone prieteni",0);
            }

        }



        runtime.reset();
        while(opModeIsActive()&& runtime.seconds()<13) {
            skystonedetectXY.updateTF();
            imuSensor.readDevice();

//            angle_to_skystone=angles.get(index);
//            xtothecenter = skystonedetectXY.distX();
//            ytothecenter = skystonedetectXY.distY();

            mecanumDriver.drive(0.02,0,0); //aici nu sunt prea sigura;
            ///tre sa existe o metoda prin care sa mearga robotul pana la skystone fara a folosi timoul(e imprecis),,dar nu o gasesc

        }


        positionController.setDirection(0 , 0);
        skystonedetectXY.TFclose();

    }
}