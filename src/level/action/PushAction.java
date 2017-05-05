package level.action;

import level.Location;
import level.Direction;

public class PushAction extends Action {
	
	private Direction agentDir;
	private Direction boxDir;
	
	public PushAction(Direction agentDir, Direction boxDir, Location location)
	{
		super(ActionType.PUSH, location);
		this.agentDir = agentDir;
		this.boxDir = boxDir;
	}
	
	public Direction getAgentDir()
	{
		return agentDir;
	}
	
	public Direction getBoxDir()
	{
		return boxDir;
	}
	
	@Override
	public String toString()
	{
		return "Push(" + Direction.toString(agentDir) + "," 
				+ Direction.toString(boxDir) + ")";
	}
	
	@Override
	public boolean isOpposite(Action action) {
		if (action instanceof PullAction)
		{
			PullAction other = (PullAction) action;
			return Direction.isOpposite(this.getAgentDir(), other.getAgentDir()) 
					&& this.getBoxDir().equals(other.getBoxDir());
		}
		else
		{
			return false;			
		}
	}

}
