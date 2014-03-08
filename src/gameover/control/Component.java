package gameover.control;

import gameover.model.Element;

public abstract class Component {

	abstract public void pre(Element e);
	abstract public void apply(Element e);
	abstract public void post(Element e);

}
