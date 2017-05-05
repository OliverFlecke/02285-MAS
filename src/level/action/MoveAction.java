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
