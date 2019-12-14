package org.firstinspires.ftc.clockworks.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.clockworks.algorithm.VuforiaPositioningSystem;

@Disabled
@TeleOp(name = "TensorFlowTeleOP", group = "Concept")
public class VuforiaTestingTeleOp extends LinearOpMode {
    VuforiaPositioningSystem vuforia;

   // private static final String VUFORIA_KEY ="AR9lPRD/////AAABmZqtUVEPuktBoKK/etgDTXIdRX+D8GG+3zaVUpNCzHeiHVrpeW1TnydidgIRxf4gck6E9/IV9T0NxeAQxH5rQ5mogSeoAuebAm7IbwZJtFuOpKWvN5uK+pFG1GZSFM7Gyz97UOqurh4oYcFGyRrJcIA6GHME7NpHkP4kN8sUm0bqAsv3MkBcVE1rocnrP1TfO7IU4ViPNuBCjsONIHY9CLmyneTaJo8XkdwqJnpvoZzBywMexfn1NhIOfTcCUUNRPNigTV53MvsSsv9w1DURIvp2fCAmnkUs2f3xnw/Rk3txw8Q376UkWbAyEpwAbTitP+8VQ/lspIKYVeaxmZ9ZOdMYVAOLEHqb58ABCLZQgSeF";
   // parameters.vuforiaLicenseKey = VUFORIA_KEY;

    @Override
    public void runOpMode() {
        vuforia.init();
        while (!isStopRequested()) {
            vuforia.update();
        }
    }
}
