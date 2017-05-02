package level;

import jason.environment.grid.Location;

public enum Direction {

	NORTH,
	WEST,
	EAST,
	SOUTH;
	
	public static final Direction[] EVERY = {
			NORTH, SOUTH, WEST, EAST
	};
	
	public static String toString(Direction dir)
	{
		switch (dir)
		{
		case NORTH	: return "N";
		case SOUTH	: return "S";
		case WEST	: return "W";
		case EAST	: return "E";
		}
		throw new UnsupportedOperationException("Invalid direction");
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
	
	public static boolean isOpposite(Direction d1, Direction d2) {
		return d1.ordinal() + d2.ordinal() == 3;
	}
}
