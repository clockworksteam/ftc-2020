package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Driver class for the HC-SR04 ultrasonic distance sensor.
 * Requires both RX and TX pins to exist in the hardware map.
 */
public class DistanceSensor {
    private com.qualcomm.robotcore.hardware.DistanceSensor distanceSensor;
    private Telemetry telemetry = null;
    double distanceCm = 0;
    /**
     * Initializes the HC-SR04 distance sensor
     * @param telemetry the telemetry logger
     * @param hardwareMap the hardware map. Must contain baseHardwareName
     * @param baseHardwareName the name of the device
     * @throws IllegalArgumentException when the hardware map does not contain baseHardwareName+"_tx" and baseHardwareName+"_rx"
     */
    public void init(Telemetry telemetry, HardwareMap hardwareMap, String baseHardwareName) {
        this.telemetry = telemetry;
        distanceSensor = hardwareMap.get(com.qualcomm.robotcore.hardware.DistanceSensor.class, baseHardwareName);
    }

    /**
     * Read the distance sensor
     */
    public void readDevice() {
        distanceCm = distanceSensor.getDistance(DistanceUnit.MM);
    }

    /**
     * Get the distance
     * @return
     */
    public double getDistance() {
        return distanceCm;
    }
}
