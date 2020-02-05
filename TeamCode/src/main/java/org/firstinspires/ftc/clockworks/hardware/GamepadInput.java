package org.firstinspires.ftc.clockworks.hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.clockworks.helpers.AngleHelper;

import java.util.HashMap;
import java.util.List;


/**
 * GamepadInput class used for getting the angle and the amplitude of the joysticks.
 */
public class GamepadInput implements Runnable {
	private Gamepad gp;
	private double angleRight, amplitudeRight, angleRight_to_return, amplitudeRight_to_return;
	private double angleLeft, amplitudeLeft, angleLeft_to_return, amplitudeLeft_to_return;
	private boolean synchronized_values;

	private HashMap<String, Boolean> states = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> last_access = new HashMap<String, Boolean>();
	private HashMap<String, Integer> pressed = new HashMap<String, Integer>();
	private HashMap<String, Integer> released = new HashMap<String, Integer>();
	private boolean running = false;

	Runnable parallel_processing = null;
	Thread parallel = null;

	private boolean aux;
	private float aux2;

	enum ACTION {
		ADD,
		ACCESS,
	}

	;


	/**
	 * Initializes the IMU sensor
	 *
	 * @param gamepad object representing the desired gamepad.
	 */
	public void init(Gamepad gamepad, GamepadInput gamepadInput) {
		parallel_processing = gamepadInput;
		parallel = new Thread(parallel_processing);


		gp = gamepad;

		parallel.run();
	}

	/**
	 * Method for getting the current joysticks' status
	 */
	private synchronized void calculate_stick(ACTION action) {

		switch (action) {

			case ADD:
				angleLeft = Math.atan2(-gp.left_stick_y, gp.left_stick_x) - Math.PI / 2;
				angleRight = Math.atan2(-gp.right_stick_y, gp.right_stick_x) - Math.PI / 2;
				angleLeft = (angleLeft * 180) / Math.PI;
				angleRight = (angleRight * 180) / Math.PI;
				angleLeft = AngleHelper.norm(angleLeft);
				angleRight = AngleHelper.norm(angleRight);
				amplitudeLeft = Math.sqrt(gp.left_stick_x * gp.left_stick_x + gp.left_stick_y * gp.left_stick_y);
				amplitudeRight = Math.sqrt(gp.right_stick_x * gp.right_stick_x + gp.right_stick_y * gp.right_stick_y);

				synchronized_values = false;
				break;

			case ACCESS:
				if (!synchronized_values) {
					angleLeft_to_return = angleLeft;
					angleRight_to_return = angleRight;
					amplitudeLeft_to_return = amplitudeLeft;
					amplitudeRight_to_return = amplitudeRight;

					synchronized_values = true;
					break;
				}
		}
	}


	/**
	 * @return the angle of the left joystick
	 */
	public double getAngleLeft() {
		calculate_stick(ACTION.ACCESS);

		return angleLeft_to_return;
	}

	/**
	 * @return the angle of the right joystick
	 */
	public double getAngleRight() {
		calculate_stick(ACTION.ACCESS);

		return angleRight_to_return;
	}

	/**
	 * @return the amplitude of the right joystick
	 */
	public double getAmplitudeRight() {
		calculate_stick(ACTION.ACCESS);

		return amplitudeRight_to_return;
	}

	/**
	 * @return the amplitude of the left joystick
	 */
	public double getAmplitudeLeft() {
		calculate_stick(ACTION.ACCESS);

		return amplitudeLeft_to_return;
	}

	public boolean was_pressed(String button){
		return access_modify_pressed_vector(ACTION.ACCESS , button);
	}

	public boolean was_released(String button){
		return access_modify_released_vector(ACTION.ACCESS , button);
	}

	public boolean get_thread_stat() {
		return running;
	}

	public void set_thread_stat(boolean val) {
		running = val;
	}

	private synchronized boolean access_modify_pressed_vector(ACTION action, String button) {
		boolean to_return = false;

		switch (action) {

			case ADD:
				int aux_here = pressed.get(button);
				pressed.put(button, (aux_here + 1));
				to_return = true;
				break;

			case ACCESS:
				if (pressed.get(button) > 0 && last_access.get(button) == false) {
					to_return = true;
					last_access.put(button, true);
					int aux_here_2 = pressed.get(button);
					pressed.put(button, (aux_here_2 - 1));
				}


		}

		return to_return;
	}

	private synchronized boolean access_modify_released_vector(ACTION action, String button) {
		boolean to_return = false;

		switch (action) {

			case ADD:
				int aux_here = released.get(button);
				released.put(button, (aux_here + 1));
				to_return = true;
				break;

			case ACCESS:
				if (released.get(button) > 0 && last_access.get(button) == true) {
					to_return = true;
					last_access.put(button, false);
					int aux_here_2 = released.get(button);
					released.put(button, (aux_here_2 - 1));
				}


		}

		return to_return;
	}


	@Override
	public void run() {
		long contor = 0;

		aux = false;
		aux2 = 0f;


		states.put("A", new Boolean(true));
		states.put("B", false);
		states.put("X", false);
		states.put("Y", false);
		states.put("LT", false);
		states.put("LB", false);
		states.put("RT", false);
		states.put("RB", false);
		states.put("DPAD_DOWN", false);
		states.put("DPAD_RIGHT", false);
		states.put("DPAD_UP", false);
		states.put("DPAD_LEFT", false);

		last_access.put("A", false);
		last_access.put("B", false);
		last_access.put("X", false);
		last_access.put("Y", false);
		last_access.put("LT", false);
		last_access.put("LB", false);
		last_access.put("RT", false);
		last_access.put("RB", false);
		last_access.put("DPAD_DOWN", false);
		last_access.put("DPAD_RIGHT", false);
		last_access.put("DPAD_UP", false);
		last_access.put("DPAD_LEFT", false);

		pressed.put("A", 0);
		pressed.put("B", 0);
		pressed.put("X", 0);
		pressed.put("Y", 0);
		pressed.put("LT", 0);
		pressed.put("LB", 0);
		pressed.put("RT", 0);
		pressed.put("RB", 0);
		pressed.put("DPAD_DOWN", 0);
		pressed.put("DPAD_RIGHT", 0);
		pressed.put("DPAD_UP", 0);
		pressed.put("DPAD_LEFT", 0);

		released.put("A", 0);
		released.put("B", 0);
		released.put("C", 0);
		released.put("D", 0);
		released.put("LT", 0);
		released.put("LB", 0);
		released.put("RT", 0);
		released.put("RB", 0);
		released.put("DPAD_DOWN", 0);
		released.put("DPAD_RIGHT", 0);
		released.put("DPAD_UP", 0);
		released.put("DPAD_LEFT", 0);

		set_thread_stat(true);

		while (running) {
		//	System.out.println(gp.a);

			calculate_stick(ACTION.ADD);

			aux = gp.a;


			//System.out.println(aux);

			if (System.currentTimeMillis() > (contor + 1000)){
				System.out.println(states.get("A").booleanValue());
				contor = System.currentTimeMillis();
			}




			if (aux) {

				System.out.println("Entered");

				if (!states.get("A")) {
					access_modify_pressed_vector(ACTION.ADD, "A");
				}
			} else if(states.get("A")) {
				access_modify_pressed_vector(ACTION.ADD, "A");
			}
			states.put("A" , aux);




		}
	}
}