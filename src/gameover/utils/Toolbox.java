package gameover.utils;

/**
 *
 * This file is part of GameOverLibrary
 *
 * Copyright 2014 Massimo Avvisati
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PConstants;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Toolbox {
	ArrayList<Button> buttons = new ArrayList<Button>();
	HashMap<String, Method> commands = new HashMap<String, Method>();

	private PVector mousePosition = new PVector();
	private boolean mousePressed;

	static PApplet parent;

	public Toolbox(PApplet _parent) {
		parent = _parent;
		parent.registerMethod("mouseEvent", this);
		parent.registerMethod("pre", this);
		parent.registerMethod("draw", this);
	}

	public void pre() {
		update();
	}

	public void draw() {
		parent.draw();
		display(parent.g);
	}

	public void display(PGraphics pg) {
		int currentImageMode = pg.imageMode;
		if (pg != parent.g)
			pg.beginDraw();

		pg.imageMode(PConstants.CENTER);

		for (Button b : buttons)
			b.display(pg);

		pg.imageMode(currentImageMode);

		if (pg != parent.g)
			pg.endDraw();
	}

	private void update() {
		if (mousePressed) {
			mousePressed = false;
			for (Button b : buttons) {
				if (b.isInside(mousePosition)) {
					executeCommand(b.getCommand(), b);
					return;
				}
			}
			
		}
	}

	public void mouseEvent(MouseEvent event) {
		mousePosition.x = event.getX();
		mousePosition.y = event.getY();

		switch (event.getAction()) {
		case MouseEvent.PRESS:
			// do something for the mouse being pressed
			mousePressed = true;
			break;
		case MouseEvent.RELEASE:
			// do something for mouse released
			break;
		case MouseEvent.CLICK:
			// do something for mouse clicked
			break;
		case MouseEvent.DRAG:
			// do something for mouse dragged
			break;
		case MouseEvent.MOVE:
			// umm... forgot
			break;
		}
	}

	public Button addButton(String command, String iconFilename, float x,
			float y) {
		return addButton(command, iconFilename, new PVector(x, y));
	}

	public Button addButton(String command, String iconFilename,
			PVector position) {
		addCommand(command);
		Button newButton = new Button(command, iconFilename, position);
		buttons.add(newButton);
		return newButton;
	}

	private void addCommand(String command) {

		Method commandMethod;
		try {
			commandMethod = parent.getClass().getMethod(command,
					new Class[] { Button.class });
		} catch (Exception e) {
			// no such method, or an error.. which is fine, just ignore
			commandMethod = null;
			PApplet.println("Please implement a " + e.getMessage()
					+ " method in your main sketch");
		}
		commands.put(command, commandMethod);
	}

	public void executeCommand(String command) {
		executeCommand(command, null);
	}

	public void executeCommand(String command, Button b) {
		if (commands.containsKey(command))
			try {
				Method commandMethod = commands.get(command);
				if (commandMethod != null) {
					commandMethod.invoke(parent, b);
				}
			} catch (Exception e) {
				PApplet.println("Cannot executeCommand(" + command + ")");
				e.printStackTrace();
			}
	}

	public class Button {
		PImage buttonImage;
		public PVector position = new PVector();
		String command;
		public boolean active = true;
		private int halfWidth;
		private int halfHeight;
		public boolean isCircular = false;

		public Button(String _command, String imageFilename, float x, float y) {
			this(_command, imageFilename, new PVector(x, y));
		}

		public Button(String _command, String imageFilename, PVector pos) {
			command = _command;
			changeIcon(imageFilename);
			position = pos.get();
		}

		public void changeIcon(String imageFilename) {
			buttonImage = Toolbox.getIcon(imageFilename);
			halfWidth = buttonImage.width / 2;
			halfHeight = buttonImage.height / 2;
		}

		private void display(PGraphics pg) {
			if (!active)
				return;

			pg.pushMatrix();
			pg.translate(position.x, position.y);
			pg.image(buttonImage, 0, 0);
			pg.popMatrix();
		}

		String getCommand() {
			return command;
		}

		boolean isInside(PVector p) {
			if (!active)
				return false;

			// TODO better isInside function using color picking with an
			// offscreen buffer
			if (!isCircular)
				return (PApplet.abs(position.x - p.x) < halfWidth && PApplet
						.abs(position.y - p.y) < halfHeight);
			else
				return (PVector.dist(p, position) < halfWidth);
		}
	}

	public static PImage getIcon(String filename) {
		// TODO Auto-generated method stub
		return parent.loadImage(filename);
	}
}
