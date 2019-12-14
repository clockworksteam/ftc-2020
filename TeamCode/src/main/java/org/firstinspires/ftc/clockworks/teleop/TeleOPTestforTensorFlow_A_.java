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
 * robotul gaseste skystone.ul ,nu isi schimba orientarea si merge drept(cu unghiul dat de angles) pana la el
 */
@TeleOp(name = "TeleOPTestforTensorFlow_A_", group = "Drive")
public class TeleOPTestforTensorFlow_A_ extends LinearOpMode {
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
    private boolean found, to_enter = false;

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
                positionController.update();

                for (String stone : labels) {
                    if (stone.contentEquals("Skystone")) {
                   //     positionController.setDirection(-90, 0.03);
                        xtothecenter = skystonedetectXY.distX();
           //             index = stone.indexOf(stone);
                        found=true;
//                        if (Math.abs(xtothecenter.get(index)) < 30) {
//                            found = true;
//                        }



                    }

                }
            }


        }


        positionController.setDirection(0 ,0);
        positionController.update();
        found = false;
        skystonedetectXY.updateTF();
        imuSensor.readDevice();


        while (opModeIsActive() && found == false){

            System.out.println("intrat2");

            skystonedetectXY.updateTF();
            labels = skystonedetectXY.type_stone();
            angles = skystonedetectXY.get_angles();
            for (String stone : labels) {
                if (stone.contentEquals("Skystone")) {
                    index = stone.indexOf(stone);
                    angle_to_skystone = angles.get(index);
                    found = true;

                }
            }
        }


        System.out.println("Angle_to_skystone: " + angle_to_skystone);
        positionController.setDirection(-angle_to_skystone,0.02);//voi presupune ca skystone.ul se afla in stanga lui

        found=false;

        while(opModeIsActive()&& found==false) {
            imuSensor.readDevice();
            positionController.update();

            skystonedetectXY.updateTF();
            labels = skystonedetectXY.type_stone();
            ///robotul nu isi schimba orientarea ,ci incepe sa mearga catre robot cu unghiul "angle_to_skystone
            //    angle_to_skystone=angles.get(index); //am presupus ca unghiul nu e mai mare de180 de grade ,deci nu trebuie bagat in norm()


            xtothecenter = skystonedetectXY.distX();
            ytothecenter = skystonedetectXY.distY();

            for (String stone : labels) {
                if (stone.contentEquals("Skystone")) {
                    index = stone.indexOf(stone);
                    to_enter = true;
                } else to_enter = false;
            }

            System.out.println("intrat3");

            if (xtothecenter.get(index) < -30 && to_enter)
                found = true;
            }


//        while(opModeIsActive()&& found==false) {
//
//
//            heading1=norm (imuSensor.getHeading());
//            /*if(heading1>0&&heading1<=180)
//            {
//                heading1+=angles.get(index);
//            }
//            else
//            {
//                if(heading1<0&&heading1>=-180)
//                {
//                    heading1-=angles.get(index);
//                }
//            }*/
//            heading1-=angles.get(index);
//            heading1=norm (heading1);
//            positionController.setHeading(heading1);
//
//            positionController.setDirection(0, 0);
//            positionController.update();
//
//            imuSensor.readDevice();
//            skystonedetectXY.updateTF();
//            telemetry.addData("angles(index) ",angles.get(index));
//            telemetry.addData("distx ",xtothecenter );
//
//            xtothecenter = skystonedetectXY.distX();
//
//
//             if(angles.get(index)<5)
//             {
//                 found=true;
//             }
//        }


        //while()



        positionController.setDirection(180 , 0);
        skystonedetectXY.TFclose();

    }




    private void sleepAndUpdate(long millis) {
        long stop = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < stop && opModeIsActive()) {
            imuSensor.readDevice();
            positionController.update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // nothing
            }
        }
    }
}