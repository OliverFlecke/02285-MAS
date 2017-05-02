package srch;

import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import level.Direction;

public abstract class Node {
	
	private Node parent;	
	private Direction direction;
	private Location location;	
	private int object, g, step;
	
	public Node(Location initial, int initialStep)
	{
		this(initial, 0, initialStep);
	}
	
	public Node(Location initial, int object, int initialStep)
	{
		this.parent 	= null;
		this.direction 	= null;
		this.location 	= initial;
		this.object		= object | WorldModel.WALL;
		this.g 			= 0;
		this.step 		= initialStep;
	}
	
	public Node(Node parent, Direction direction, Location location) 
	{
		this.parent    	= parent;
		this.direction 	= direction;
		this.location  	= location;
		this.object	   	= parent.object;
		this.g         	= parent.g + 1;
		this.step 		= parent.getStep() + 1;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public int getObject() {
		return object;
	}
	
	public int g() {
		return g;
	}
	
	public int getStep()
	{
		return this.step;
	}

	public abstract List<Node> getExpandedNodes();

	public abstract <T> T extractPlan();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
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
		Node other = (Node) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}	
}
