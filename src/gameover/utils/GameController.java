package gameover.utils;

import java.lang.reflect.Method;
import java.util.HashMap;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.EventQueue;
import processing.core.PApplet;
import processing.core.PVector;

public class GameController {

	static ControllerEnvironment ce;
	Controller[] controllers;

	PApplet parent;

	class Gamepad {
		String componentName;

		boolean[] buttons = new boolean[4];

		boolean[] directions = new boolean[4];
		PVector joystickPosition = new PVector();

		Gamepad(String name) {
			componentName = name;
		}
	}

	Gamepad[] gamepads;
	static final int NONE = -666;

	public GameController(PApplet _parent) {
		parent = _parent;
		try {
			if (ControllerEnvironment.getDefaultEnvironment().isSupported()) {
				ce = ControllerEnvironment.getDefaultEnvironment();
				controllers = ce.getControllers();
				// TODO filter controllers that are not gamepads or joysticks
				gamepads = new Gamepad[controllers.length];
				for (int i = 0; i < controllers.length; i++) {
					gamepads[i] = new Gamepad(controllers[i].getName());

				}
			}
		} catch (Exception ex) {
			PApplet.println("No joystick or gamepad connected! Disabling all GameController objects!");
		}
		if (controllers != null) {
			parent.registerMethod("pre", this);
			addCommand("pad");
		}
	}

	public int getPadsNumber() {
		if (controllers != null)
			return controllers.length;
		else
			return 0;
	}

	public void pre() {
		update();
		dispatchEvents();
	}

	private void dispatch(int padID, PVector position, boolean[] directions,
			boolean... buttons) {
		// PApplet.println(parent.millis() + " - " + padID + ":" + position +
		// " | " + directions + " buttons: " + buttons);
		int currentDirection = NONE;
		if (directions[PAD_LEFT] && directions[PAD_UP]) {
			currentDirection = PAD_UP_LEFT;
		} else if (directions[PAD_LEFT] && directions[PAD_DOWN]) {
			currentDirection = PAD_DOWN_LEFT;
		} else if (directions[PAD_RIGHT] && directions[PAD_UP]) {
			currentDirection = PAD_UP_RIGHT;
		} else if (directions[PAD_RIGHT] && directions[PAD_DOWN]) {
			currentDirection = PAD_DOWN_RIGHT;
		} else if (directions[PAD_RIGHT]) {
			currentDirection = PAD_RIGHT;
		} else if (directions[PAD_LEFT]) {
			currentDirection = PAD_LEFT;
		} else if (directions[PAD_DOWN]) {
			currentDirection = PAD_DOWN;
		} else if (directions[PAD_UP]) {
			currentDirection = PAD_UP;
		}

		if (commands.containsKey("pad")) {
			Method m = commands.get("pad");
			try {
				m.invoke(parent, new Object[] { padID, currentDirection });
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	HashMap<String, Method> commands = new HashMap<String, Method>();

	private void addCommand(String command) {

		Method commandMethod;
		try {
			commandMethod = parent.getClass().getMethod(command,
					new Class[] { int.class, int.class });
		} catch (Exception e) {
			// no such method, or an error.. which is fine, just ignore
			commandMethod = null;
			PApplet.println("Please implement a " + e.getMessage()
					+ " method in your main sketch");
		}
		if (commandMethod != null)
			commands.put(command, commandMethod);
	}

	public static final int PAD_LEFT = 0;
	public static final int PAD_RIGHT = 1;
	public static final int PAD_UP = 2;
	public static final int PAD_DOWN = 3;
	public static final int PAD_UP_LEFT = 4;
	public static final int PAD_UP_RIGHT = 5;
	public static final int PAD_DOWN_LEFT = 6;
	public static final int PAD_DOWN_RIGHT = 7;

	private void dispatchEvents() {
		boolean[] directions = new boolean[4];

		for (int i = 0; i < gamepads.length; i++) {
			if (PApplet.abs(gamepads[i].joystickPosition.x) > 0.1)
				if (gamepads[i].joystickPosition.x > 0)
					directions[PAD_RIGHT] = true;
				else
					directions[PAD_LEFT] = true;
			if (PApplet.abs(gamepads[i].joystickPosition.y) > 0.1)
				if (gamepads[i].joystickPosition.y > 0)
					directions[PAD_DOWN] = true;
				else
					directions[PAD_UP] = true;

			if (PVector.dist(new PVector(), gamepads[i].joystickPosition) > 0.01) {
				dispatch(i, gamepads[i].joystickPosition, directions);
				gamepads[i].directions = directions;
			}
		}
	}

	public PVector getPad(int index) {
		return gamepads[index].joystickPosition;
	}

	private void updateGamepad(int index, String propertyName, float value,
			boolean isAnalog) {

		if (propertyName.equals("x"))
			gamepads[index].joystickPosition.x = value;
		if (propertyName.equals("y"))
			gamepads[index].joystickPosition.y = value;

	}

	private void update() {

		for (int i = 0; i < controllers.length; i++) {
			controllers[i].poll();

			EventQueue queue = controllers[i].getEventQueue();
			net.java.games.input.Event event = new net.java.games.input.Event();

			while (queue.getNextEvent(event)) {
				Component component = event.getComponent();
				String name = component.getName();
				float value = event.getValue();
				boolean isAnalog = component.isAnalog();
				updateGamepad(i, name, value, isAnalog);
				// event.getNanos()

			}
		}
	}
}
