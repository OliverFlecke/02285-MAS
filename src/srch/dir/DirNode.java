package srch.dir;

import java.util.ArrayList;
import java.util.LinkedList;

import env.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class DirNode extends Node {
	
	private String direction;
	
	public DirNode(Location initial)
	{
		super(initial);
		direction = null;
	}
	
	private DirNode(Node parent, String direction, Location location) 
	{
		super(parent, location);
		this.direction = direction;
	}
	
	public String getDirection() {
		return direction;
	}

	public ArrayList<Node> GetExpandedNodes() 
	{
		ArrayList<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, this.getLocation());
					
			if (WorldModel.getInstance().noWallsOrBoxes(loc))
			{
				expandedNodes.add(new DirNode(this, dir, loc));
			}
		}		
		
		return expandedNodes;
	}

	public LinkedList<String> extractPlan() 
	{
		LinkedList<String> plan = new LinkedList<String>();
		
		for (DirNode n = this; n.direction != null; n = (DirNode) n.getParent()) 
		{
			plan.addFirst(n.direction);
		}		
		
		return plan;
	}	
}
