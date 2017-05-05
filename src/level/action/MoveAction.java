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
	
	@Override
	public Action getOpposite() {
		return new MoveAction(Direction.getOpposite(this.getDirection()), Location.newLocation(this.getDirection(), this.getAgentLocation()));
	}
	
	@Override
	public boolean isOpposite(Action action) {
		return Direction.isOpposite(((MoveAction) action).getDirection(), this.getDirection());
	}
	
	@Override
	public Location getNewAgentLocation()
	{
		return Location.newLocation(this.direction, this.getAgentLocation());
	}
}
