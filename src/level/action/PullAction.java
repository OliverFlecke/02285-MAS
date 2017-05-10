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
				this.getAgentLocation().newLocation(this.getAgentDir()));
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
	
	@Override
	public Location getNewAgentLocation() {
		return Location.newLocation(this.getAgentDir(), this.getAgentLocation());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((agentDir == null) ? 0 : agentDir.hashCode());
		result = prime * result + ((boxDir == null) ? 0 : boxDir.hashCode());
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
		PullAction other = (PullAction) obj;
		if (agentDir != other.agentDir)
			return false;
		if (boxDir != other.boxDir)
			return false;
		return true;
	}
}
