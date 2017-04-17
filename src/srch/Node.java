package srch;

import java.util.List;

import jason.environment.grid.Location;

public abstract class Node {
	
	private Node parent;	
	private String direction;
	private Location location;	
	private int g;
	
	public Node(Location initial)
	{
		parent 		= null;
		direction 	= null;
		location 	= initial;
		g 			= 0;
	}
	
	public Node(Node parent, String direction, Location location) 
	{
		this.parent    = parent;
		this.direction = direction;
		this.location  = location;	
		this.g         = parent.g + 1;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public int g() {
		return g;
	}

	public abstract List<Node> getExpandedNodes();

	public abstract List<?> extractPlan();

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
