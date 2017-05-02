package level.Actions;

import level.*;

public class MoveAction extends Action {

	private Direction direction;
	
	public MoveAction(Direction direction)
	{
		super(ActionType.MOVE);
		this.direction = direction;
	}
	
	@Override
	public String toString()
	{
		return "Move(" + Direction.toString(this.direction) + ")";
	}
}
