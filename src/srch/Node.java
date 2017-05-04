package srch;

import java.util.List;

import env.model.GridWorldModel;
import level.Location;

public abstract class Node {
	
	private Node parent;	
	private Location location;	
	private int object, g;
	
	public Node(Location initial)
	{
		this(initial, 0);
	}
	
	public Node(Location initial, int object)
	{
		this.parent 	= null;
		this.location 	= initial;
		this.object		= object | GridWorldModel.WALL;
		this.g 			= 0;
	}
	
	public Node(Node parent, Location location) 
	{
		this.parent    	= parent;
		this.location  	= location;
		this.object	   	= parent.object;
		this.g         	= parent.g + 1;
	}
	
	public Node getParent() {
		return this.parent;
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

	public abstract List<? extends Node> getExpandedNodes();

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
