package level;

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

	public static boolean isOpposite(Direction d1, Direction d2) 
	{
		return d1.ordinal() + d2.ordinal() == 3;
	}

	public static Direction getOpposite(Direction direction) 
	{
		switch (direction)
		{
		case NORTH: return SOUTH;
		case SOUTH: return NORTH;
		case EAST:  return WEST;
		case WEST:  return EAST;
		}
		throw new UnsupportedOperationException("Invalid direction");
	}
}
