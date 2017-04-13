package lvl.cell;

import lvl.Color;

public abstract class Colored extends Lettered {

	private Color color;
	
	public Colored(int x, int y, char letter, String color) 
	{
		super(x, y, letter);
		
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
