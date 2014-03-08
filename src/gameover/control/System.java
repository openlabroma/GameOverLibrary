package gameover.control;

import gameover.model.Element;

import java.util.ArrayList;

public class System {
	ArrayList<Element> elements;
	ArrayList<Component> components;

	public System() {
		elements = new ArrayList<Element>();
		components = new ArrayList<Component>();
	}

	public Element addElements(Element e) {
		elements.add(e);
		return e;
	}

	public Component addComponent(Component component) {
		components.add(component);
		return component;
	}

	public void run() {
		for (gameover.model.Element e : elements) {
			for (Component c : components) {
				c.pre(e);
			}
			for (Component c : components) {
				c.apply(e);
			}
			for (Component c : components) {
				c.post(e);
			}
		}

	}

}
