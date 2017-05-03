package level.cell;

import level.Location;

public abstract class Cell {
	
	private Location location;
	
	public Cell(Location location) 
	{
		this.location = location;
	}
	
	public Cell(int x, int y) 
	{
		this.location = new Location(x, y);
	}

	public Location getLocation() 
	{
		return location;
	}

	public void setLocation(Location location) 
	{
		this.location = location;
	}
	
	public void setLocation(int x, int y) 
	{
		this.location = new Location(x, y);
	}
}
