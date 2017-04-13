package lvl;

public class Colored extends Cell {

	private String color;
	
	public Colored(int x, int y) {
		super(x, y);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color == null ? "blue" : color;
	}
	
}
