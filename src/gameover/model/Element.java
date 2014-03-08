package gameover.model;

public class Element {
	static int availableIndex = 0;
	int id;
	public Element() {
		id = generateID();
	}
	
	static public int generateID() {
		return availableIndex++;
	}
	
	@Override
	public String toString() {
		return "" + id;
	}
}
