package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.GridWorldModel;
import level.DependencyPath;
import level.Direction;
import level.Location;
import srch.Node;
import srch.interfaces.IDirectionNode;

public class DependencyPathNode extends Node implements IDirectionNode {

	private Direction direction;
	private int dependency;
	private int dependencyCount;
	private int include;
	private GridWorldModel model;

	public DependencyPathNode(Location initial, int dependency, int include, GridWorldModel model) 
	{
		super(initial);
		
		this.direction 			= null;
		this.dependency 		= dependency;
		this.dependencyCount 	= 0;
		this.include			= include;
		this.model				= model;
	}

	public DependencyPathNode(Node parent, Direction dir, Location loc) 
	{
		super(parent, loc);
		
		this.direction			= dir;
		this.dependency 		= ((DependencyPathNode) parent).dependency;
		this.dependencyCount 	= ((DependencyPathNode) parent).dependencyCount;
		this.include			= ((DependencyPathNode) parent).include;
		this.model				= ((DependencyPathNode) parent).model;
		
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
	
	public GridWorldModel getModel() {
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
				expandedNodes.add(new DependencyPathNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DependencyPath extractPlan() 
	{		
		DependencyPath path = new DependencyPath();
		
		for (Node n = this; n != null; n = n.getParent()) 
		{			
			Location loc = n.getLocation();
			
			if (n.getParent() == null)
			{
				path.addToPath(loc);
			}			
			else if (model.isFree(include, loc) && model.hasObject(dependency, loc))
			{
				path.addDependency(loc);
			}
			else
			{
				path.addToPath(loc);
			}
		}
		return path;
	}

}
