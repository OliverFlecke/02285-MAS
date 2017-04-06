package srch;

import java.util.ArrayList;
import java.util.LinkedList;

import env.WorldModel;
import jason.environment.grid.Location;

public class Node {
	
	Node parent;
	String direction;
	Location location;
	
	int g;
	
	public Node(Location initial)
	{
		parent = null;
		direction = null;
		location = initial;
		g = 0;
	}
	
	private Node(Node parent, String direction, Location location) 
	{
		this.parent    = parent;
		this.direction = direction;	
		this.location  = location;	
		this.g         = parent.g + 1;
	}

	public ArrayList<Node> GetExpandedNodes() 
	{
		ArrayList<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, location);
					
			if (WorldModel.getInstance().noWallsOrBoxes(loc))
			{
				expandedNodes.add(new Node(this, dir, loc));
			}
		}		
		
		return expandedNodes;
	}

	public LinkedList<String> extractPlan() 
	{
		LinkedList<String> plan = new LinkedList<String>();
		
		for (Node n = this; n.direction != null; n = n.parent) 
		{
			plan.addFirst(n.direction);
		}		
		
		return plan;
	}

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
