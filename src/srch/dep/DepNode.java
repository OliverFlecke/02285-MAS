package srch.dep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class DepNode extends Node {

	private int object;
	private int dependencies;

	public DepNode(Location initial, int object) 
	{
		super(initial);
		
		this.object 		= object;
		this.dependencies 	= 0;
	}

	public DepNode(Node parent, String dir, Location loc) 
	{
		super(parent, dir, loc);
		
		this.object 		= ((DepNode) parent).object;
		this.dependencies 	= ((DepNode) parent).dependencies;
		
		if (WorldModel.getInstance().hasObject(loc, object)) 
		{
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
	public List<Node> getExpandedNodes() 
	{
		List<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, this.getLocation());
			
			if (WorldModel.getInstance().isFree(loc, WorldModel.WALL))
			{
				expandedNodes.add(new DepNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}

	/**
	 * Override to extract dependency
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Location> extractPlan() 
	{
		if (this.getDependencies() == 0) return Collections.emptyList();
		
		LinkedList<Location> plan = new LinkedList<Location>();		
		
		for (Node n = this; n.getDirection() != null; n = n.getParent()) 
		{
			Location loc = n.getLocation();
			
			if (WorldModel.getInstance().hasObject(loc, object))
			{
				plan.addFirst(loc);
			}
		}
		return plan;
	}
}
