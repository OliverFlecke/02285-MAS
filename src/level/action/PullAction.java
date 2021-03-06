package level.action;

import level.Location;
import level.Direction;

public class PullAction extends Action {

	private Direction agentDir;
	private Direction boxDir;
	
	private Location boxLocation;
	private Location newBoxLocation;
	
	public PullAction(Direction agentDir, Direction boxDir, Location location)
	{
		super(ActionType.PULL, location, location.newLocation(agentDir));
		this.agentDir = agentDir;
		this.boxDir = boxDir;
		this.boxLocation 	= getAgentLocation().newLocation(boxDir);
		this.newBoxLocation = getAgentLocation();
	}
	
	protected Direction getAgentDir()
	{
		return agentDir;
	}
	
	protected Direction getBoxDir()
	{
		return boxDir;
	}
	
	public Location getBoxLocation()
	{
		return boxLocation;
	}
	
	public Location getNewBoxLocation()
	{
		return newBoxLocation;
	}
	
	@Override 
	public String toString()
	{
		return "Pull(" + this.agentDir.toString() + ","
				+ this.boxDir.toString() + ")";
	}
	
	@Override
	public Action getOpposite()
	{
		return new PushAction(this.getAgentDir().getOpposite(), this.getBoxDir(), 
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
