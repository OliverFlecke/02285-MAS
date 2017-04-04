package mas.entity;

public class Box extends WorldObject {

	public Box(char id, char row, char col, Color color) {
		super(id, row, col, color);
	}

	public Box(char chr, Color color) {
		super(chr, color);
	}

}
