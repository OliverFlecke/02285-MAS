package srch.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import env.model.CellModel;
import level.Direction;
import level.Location;
import srch.Node;
import srch.interfaces.IDirectionNode;
import srch.interfaces.IModelNode;

public class DependencyNode extends Node implements IDirectionNode, IModelNode {

	private Direction direction;
	private int dependency;
	private int dependencyCount;
	private CellModel model;

	public DependencyNode(Location initial, int dependency, CellModel model) 
	{
		super(initial);
		
		this.direction 			= null;
		this.dependency 		= dependency;
		this.dependencyCount 	= 0;
		this.model				= model;
	}

	public DependencyNode(Node parent, Direction dir, Location loc) 
	{
		super(parent, loc);
		
		DependencyNode n = (DependencyNode) parent;
		
		this.direction			= dir;
		this.dependency 		= n.dependency;
		this.dependencyCount 	= n.dependencyCount;
		this.dependencyCount 	= n.dependencyCount;
		this.model				= n.model;
		
		if (model.hasObject(dependency, loc)) 
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
	
	public CellModel getModel() {
		return model;
	}

	@Override
	public List<Node> getExpandedNodes() 
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.EVERY.length);
		
		for (Direction dir : Direction.EVERY)
		{
			Location loc = this.getLocation().newLocation(dir);
			
			if (model.isFree(this.getObject(), loc))
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
			
			if (model.hasObject(dependency, loc))
			{
				plan.addFirst(loc);
			}
		}
		return plan;
	}
}
