package srch.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import env.model.WorldModel;
import level.Direction;
import level.Location;
import srch.Node;
import srch.interfaces.IDirectionNode;

public class DependencyNode extends Node implements IDirectionNode {

	private Direction direction;
	private int dependency;
	private int dependencyCount;

	public DependencyNode(Location initial, int dependency) 
	{
		super(initial);
		
		this.direction 			= null;
		this.dependency 		= dependency;
		this.dependencyCount 	= 0;
	}

	public DependencyNode(Node parent, Direction dir, Location loc) 
	{
		super(parent, loc);
		
		this.direction			= dir;
		this.dependency 		= ((DependencyNode) parent).dependency;
		this.dependencyCount 	= ((DependencyNode) parent).dependencyCount;
		
		if (WorldModel.getInstance().hasObject(dependency, loc)) 
		{
			dependencyCount++;
		}
	}

	@Override
	public Direction getDirection() {
		return direction;
	}
	
	public int getDependencies() {
		return dependencyCount;
	}

	@Override
	public List<Node> getExpandedNodes() 
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.EVERY.length);
		
		for (Direction dir : Direction.EVERY)
		{
			Location loc = Location.newLocation(dir, this.getLocation());
			
			if (WorldModel.getInstance().isFree(this.getObject(), loc))
			{
				expandedNodes.add(new DependencyNode(this, dir, loc));
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
		
		for (Node n = this; n.getParent() != null; n = n.getParent()) 
		{
			Location loc = n.getLocation();
			
			if (WorldModel.getInstance().hasObject(dependency, loc))
			{
				plan.addFirst(loc);
			}
		}
		return plan;
	}
}
