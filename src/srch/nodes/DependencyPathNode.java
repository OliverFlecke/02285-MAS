package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.DataModel;
import env.model.WorldModel;
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
	private boolean includeLast;
	private DataModel model;

	public DependencyPathNode(Location initial, int dependency, int include, boolean includeLast, DataModel model) 
	{
		super(initial);
		
		this.direction 			= null;
		this.dependency 		= dependency;
		this.dependencyCount 	= 0;
		this.include			= include;
		this.includeLast		= includeLast;
		this.model				= model;
	}

	public DependencyPathNode(Node parent, Direction dir, Location loc) 
	{
		super(parent, loc);
		
		DependencyPathNode n = (DependencyPathNode) parent;
		
		this.direction			= dir;
		this.dependency 		= n.dependency;
		this.dependencyCount 	= n.dependencyCount;
		this.include			= n.include;
		this.includeLast		= n.includeLast;
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
	
	public DataModel getModel() {
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
		
		path.addToPath(this.getLocation());
		
		for (Node n = this; n != null; n = n.getParent()) 
		{			
			Location loc = n.getLocation();
			
			// TODO: Add agent color to search
			if (!(n == this && includeLast || n.getParent() == null) &&
				model.isFree(include, WorldModel.BOX_MASK, loc) && model.hasObject(dependency, loc))
			{
				path.addDependency(loc);
			}
			
			path.addToPath(loc);
		}
		return path;
	}

}
