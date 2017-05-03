package level.action;

import level.Location;
import level.*;

public class MoveAction extends Action {

	private Direction direction;
	
	public MoveAction(Direction direction, Location location)
	{
		super(ActionType.MOVE, location);
		this.direction = direction;
	}
	
	public Direction getDirection() 
	{
		return direction;
	}
	
	@Override
	public String toString()
	{
		return "Move(" + Direction.toString(this.direction) + ")";
	}
}
