package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.clockworks.helpers.AngleHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * GamepadInput class used for getting the angle and the amplitude of the joysticks.
 */
public class GamepadInput {
	private Gamepad gp;
	private double angleRight, amplitudeRight;
	private double angleLeft, amplitudeLeft;
	private Map<Character, Boolean> last_value = new HashMap<Character, Boolean>();
	private Map<Character , Boolean> current_value = new HashMap<Character, Boolean>();

/**
 * Initializes the IMU sensor
 * @param gamepad object representing the desired gamepad.
*/
	public void init(Gamepad gamepad) {
		gp = gamepad;
		last_value.put('A' , false);
		last_value.put('B' , false);
		last_value.put('X' , false);
		last_value.put('Y' , false);
	}

/**
 * Method for getting the current joysticks' status
*/
	public void readDevice() {
		angleLeft = Math.atan2(-gp.left_stick_y, gp.left_stick_x) - Math.PI / 2;
		angleRight = Math.atan2(-gp.right_stick_y, gp.right_stick_x) - Math.PI / 2 ;
		angleLeft = (angleLeft * 180) / Math.PI;
		angleRight = (angleRight * 180) / Math.PI;
		angleLeft = AngleHelper.norm(angleLeft);
		angleRight = AngleHelper.norm(angleRight);
		amplitudeLeft = Math.sqrt(gp.left_stick_x * gp.left_stick_x + gp.left_stick_y * gp.left_stick_y);
		amplitudeRight = Math.sqrt(gp.right_stick_x * gp.right_stick_x + gp.right_stick_y * gp.right_stick_y);
	}


	public void readDevice_button(){

		current_value.put('A' , gp.a);
//		System.out.println(current_value);
		current_value.put('B' , gp.b);
		current_value.put('X' , gp.x);
		current_value.put('Y' , gp.y);
	}


	public boolean was_pressed(char button){

		if (current_value.get(button) && (!last_value.get(button)))
			return true;
		return false;
	}

	public void readDevice_button_2(){
		last_value.put('A' , current_value.get('A'));
		last_value.put('B' , current_value.get('B'));
		last_value.put('X' , current_value.get('X'));
		last_value.put('Y' , current_value.get('Y'));
	}






/** 
 * @return the angle of the left joystick
 */
	public double getAngleLeft() {
		return angleLeft;
	}

/** 
 * @return the angle of the right joystick
 */
	public double getAngleRight() {
		return angleRight;
	}

/** 
 * @return the amplitude of the right joystick
 */
	public double getAmplitudeRight() {
		return amplitudeRight;
	}

/** 
 * @return the amplitude of the left joystick
 */
	public double getAmplitudeLeft() {
		return amplitudeLeft;
	}

}
