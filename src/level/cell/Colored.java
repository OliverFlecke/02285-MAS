package level.cell;

import jason.environment.grid.Location;
import level.Color;

public abstract class Colored extends Lettered {

	private Color color;
	
	public Colored(Location location, char letter, Color color) 
	{
		super(location, letter);
		
//		this.color = Color.getColor(color == null ? "" : color);
		this.color = color;
	}
	
	public Colored(int x, int y, char letter, Color color) 
	{
		this(new Location(x, y), letter, color);
	}
	
	/**
	 * @return The color of this object
	 */
	public Color getColor()
	{
		return color;
	}
}
