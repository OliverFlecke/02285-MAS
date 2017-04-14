package srch.dep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import env.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class DepNode extends Node {
	
	private int dependencies;

	public DepNode(Location initial) {
		super(initial);
		dependencies = 0;
	}

	public DepNode(Node parent, String dir, Location loc) {
		super(parent, dir, loc);
		
		if (WorldModel.getInstance().hasObject(WorldModel.GOAL, loc)) {
			dependencies++;
		}
	}
	
	public int getDependencies() {
		return dependencies;
	}

	/**
	 * Override to only account for walls
	 */
	@Override
	public ArrayList<Node> getExpandedNodes() 
	{
		ArrayList<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, this.getLocation());
			
			if (WorldModel.getInstance().isFreeOfObstacle(loc))
			{
				expandedNodes.add(new DepNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}

	/**
	 * Override to extract dependency
	 */
	@Override
	public List<Location> extractPlan() 
	{
		if (this.getDependencies() == 0) return Collections.emptyList();
		
		LinkedList<Location> plan = new LinkedList<Location>();		
		
		for (DepNode n = this; n.getDirection() != null; n = (DepNode) n.getParent()) 
		{
			Location loc = n.getLocation();
			
			if (WorldModel.getInstance().hasObject(WorldModel.GOAL, loc))
			{
				plan.addFirst(loc);
			}
		}
		return plan;
	}
}
