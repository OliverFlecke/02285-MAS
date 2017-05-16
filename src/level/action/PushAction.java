package level.action;

import level.Location;
import level.Direction;

public class PushAction extends Action {
	
	private Direction agentDir;
	private Direction boxDir;
	
	public PushAction(Direction agentDir, Direction boxDir, Location location)
	{
		super(ActionType.PUSH, location, location.newLocation(boxDir));
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
		return "Push(" + agentDir.toString() + "," 
				+ boxDir.toString() + ")";
	}
	
	@Override
	public Action getOpposite()
	{
		return new PullAction(this.getAgentDir().getOpposite(), this.getBoxDir(), 
				this.getAgentLocation().newLocation(this.getAgentDir()));
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
		PushAction other = (PushAction) obj;
		if (agentDir != other.agentDir)
			return false;
		if (boxDir != other.boxDir)
			return false;
		return true;
	}

}
