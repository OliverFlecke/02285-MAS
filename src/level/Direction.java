package level;

public enum Direction {

	NORTH,
	WEST,
	EAST,
	SOUTH,
	NORTH_EAST,
	NORTH_WEST,
	SOUTH_EAST,
	SOUTH_WEST;
	
	public static final Direction[] EVERY = {
			NORTH, SOUTH, WEST, EAST
	};
	
	public String toString()
	{
		switch (this)
		{
		case NORTH	: return "N";
		case SOUTH	: return "S";
		case WEST	: return "W";
		case EAST	: return "E";
		default: 	  throw new IllegalArgumentException("Invalid direction");
		}
		
	}

	public static boolean isOpposite(Direction d1, Direction d2) 
	{
		return d1.ordinal() + d2.ordinal() == 3;
	}

	public Direction getOpposite() 
	{
		switch (this)
		{
		case NORTH: return SOUTH;
		case SOUTH: return NORTH;
		case EAST:  return WEST;
		case WEST:  return EAST;
		default: 	throw new IllegalArgumentException("Invalid direction");
		}
		
	}
	
	public boolean hasDirection(Direction other)
	{
		switch (this)
		{
		case EAST: 			return other == EAST || other == NORTH_EAST || other == SOUTH_EAST;
		case NORTH:			return other == NORTH || other == NORTH_EAST || other == NORTH_WEST;
		case NORTH_EAST:	return other == NORTH_EAST || other == NORTH || other == EAST;
		case NORTH_WEST:	return other == NORTH_WEST || other == NORTH || other == WEST;
		case SOUTH:			return other == SOUTH || other == SOUTH_EAST || other == SOUTH_WEST;
		case SOUTH_EAST:	return other == SOUTH_EAST || other == SOUTH || other == EAST;
		case SOUTH_WEST:	return other == SOUTH_WEST || other == SOUTH || other == WEST;
		case WEST:			return other == WEST || other == NORTH_WEST || other == SOUTH_WEST;
		default: 			throw new IllegalArgumentException("Invalid direction");
		}
	}
}
