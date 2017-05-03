package level.action;

import java.util.LinkedList;
import java.util.List;

import level.Location;
import level.Direction;

public abstract class Action {

	public enum ActionType {
		MOVE,
		PUSH, 
		PULL,
		SKIP
	}
	
	private ActionType type;
	private Location agentLocation;
	
	protected Action(ActionType type, Location location)
	{
		this.type = type;
		this.agentLocation = location;
	}

	public ActionType getType()
	{
		return this.type;
	}
	
	public Location getAgentLocation()
	{
		return this.agentLocation;
	}
	
	public static List<Action> Every(Location agentLocation)
	{
		LinkedList<Action> actions = new LinkedList<Action>();
		for (Direction dir : Direction.EVERY) 
			actions.add(new MoveAction(dir, agentLocation));
		
		for (Direction d1 : Direction.EVERY) 
			for (Direction d2 : Direction.EVERY) 
				if (!Direction.isOpposite(d1, d2)) 
					actions.add(new PushAction(d1, d2, agentLocation));
		
		for (Direction d1 : Direction.EVERY) 
			for (Direction d2 : Direction.EVERY)
				if (d1 != d2) 
					actions.add(new PullAction(d1, d2, agentLocation));
		
		actions.add(new SkipAction(agentLocation));
		return actions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agentLocation == null) ? 0 : agentLocation.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (agentLocation == null) {
			if (other.agentLocation != null)
				return false;
		} else if (!agentLocation.equals(other.agentLocation))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
