package level;

public class Location {

	public int x, y;
	
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Location(Location location)
	{
		this.x = location.x;
		this.y = location.y;
	}
	
	public int distance(Location other)
	{
		return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
	}
	
	/**
	 * Computes a new Location based on current direction 
	 * and location.
	 * @param dir - Direction
	 * @param l - Location
	 * @return The new Location.
	 */
	public static Location newLocation(Direction dir, Location l)
	{        
	    switch (dir) 
	    {
	    case NORTH	: return new Location(l.x, l.y - 1);
	    case SOUTH	: return new Location(l.x, l.y + 1);
	    case WEST	: return new Location(l.x - 1, l.y);
	    case EAST	: return new Location(l.x + 1, l.y);
	    }        
	    return null; // Could return l
	}
}
