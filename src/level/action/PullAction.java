package level.action;

import level.Location;
import level.Direction;

public class PullAction extends Action {

	private Direction agentDir;
	private Direction boxDir;
	
	public PullAction(Direction agentDir, Direction boxDir, Location location)
	{
		super(ActionType.PULL, location);
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
		return "Pull(" + Direction.toString(this.agentDir) + ","
				+ Direction.toString(this.boxDir) + ")";
	}
	
	@Override
	public Action getOpposite()
	{
		return new PushAction(Direction.getOpposite(this.getAgentDir()), this.getBoxDir(), 
				Location.newLocation(this.getAgentDir(), this.getAgentLocation()));
	}
	
	@Override
	public boolean isOpposite(Action action) {
		if (action instanceof PushAction)
		{
			PushAction other = (PushAction) action;
			return Direction.isOpposite(this.getAgentDir(), other.getAgentDir()) 
					&& this.getBoxDir().equals(other.getBoxDir());
		}
		else
		{
			return false;			
		}
	}
}
