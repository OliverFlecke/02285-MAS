package level.Actions;

import level.Direction;

public class PullAction extends Action {

	private Direction agentDir;
	private Direction boxDir;
	
	public PullAction(Direction agentDir, Direction boxDir)
	{
		super(ActionType.PULL);
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
}
