package lvl;

public abstract class Colored extends Cell {

	private Color color;
	
	public Colored(int x, int y, String color) {
		super(x, y);
		this.color = Color.getColor(color);
	}
	
	/**
	 * @return The color of this object
	 */
	public Color getColor()
	{
		return this.color;
	}
}
