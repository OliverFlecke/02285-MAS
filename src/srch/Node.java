package srch;

import java.util.ArrayList;
import java.util.LinkedList;

import jason.environment.grid.Location;

public abstract class Node {
	
	private Node parent;
	private Location location;	
	private int g;
	
	public Node(Location initial)
	{
		parent = null;
		location = initial;
		g = 0;
	}
	
	public Node(Node parent, Location location) 
	{
		this.parent    = parent;
		this.location  = location;	
		this.g         = parent.g + 1;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public int g() {
		return g;
	}

	public abstract ArrayList<Node> GetExpandedNodes();

	public abstract LinkedList<String> extractPlan();

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
