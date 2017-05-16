package level.action;

import level.Location;
import level.*;

public class MoveAction extends Action {

	private Direction direction;
	
	public MoveAction(Direction direction, Location location)
	{
		super(ActionType.MOVE, location, location);
		this.direction = direction;
	}
	
	public Direction getDirection() 
	{
		return direction;
	}
	
	@Override
	public String toString()
	{
		return "Move(" + this.direction.toString() + ")";
	}
	
	@Override
	public Action getOpposite() {
		return new MoveAction(this.getDirection().getOpposite(), this.getAgentLocation().newLocation(this.getDirection()));
	}
	
	@Override
	public boolean isOpposite(Action action) {
		return action instanceof MoveAction ? Direction.isOpposite(((MoveAction) action).getDirection(), this.getDirection()) : false;
	}
	
	@Override
	public Location getNewAgentLocation()
	{
		return Location.newLocation(this.direction, this.getAgentLocation());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoveAction other = (MoveAction) obj;
		if (direction != other.direction)
			return false;
		return true;
	}
}
