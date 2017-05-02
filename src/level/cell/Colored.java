package level.cell;

import jason.environment.grid.Location;
import level.Color;

public abstract class Colored extends Lettered {

	private Color color;
	
	public Colored(Location location, char letter, String color) 
	{
		super(location, letter);
		
		this.color = Color.getColor(color == null ? "" : color);
	}
	
	public Colored(int x, int y, char letter, String color) 
	{
		this(new Location(x, y), letter, color);
	}
	
	/**
	 * @return The color of this object
	 */
	public String getColor()
	{
		return color.toString().toLowerCase();
	}
}
