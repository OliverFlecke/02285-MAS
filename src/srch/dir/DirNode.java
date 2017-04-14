package srch.dir;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class DirNode extends Node {

	public DirNode(Location initial) {
		super(initial);
	}

	public DirNode(Node parent, String direction, Location location) {
		super(parent, direction, location);
	}

	public List<Node> getExpandedNodes() 
	{
		List<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
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

	@Override
	public List<String> extractPlan() 
	{
		LinkedList<String> plan = new LinkedList<String>();
		
		for (DirNode n = this; n.getDirection() != null; n = (DirNode) n.getParent()) 
		{
			plan.addFirst(n.getDirection());
		}		
		
		return plan;
	}
}
