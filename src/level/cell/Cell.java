package level.cell;

import jason.environment.grid.Location;

public class Cell {
	
	private Location location;
	
	public Cell(Location location) 
	{
		this.location = location;
	}
	
	public Cell(int x, int y) 
	{
		this.location = new Location(x, y);
	}
	
	public Cell(Cell cell)
	{
		this.location = new Location(cell.location.x, cell.location.y);
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
