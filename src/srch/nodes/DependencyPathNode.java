package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.WorldModel;
import env.planner.Planner;
import level.DependencyPath;
import level.Direction;
import level.Location;
import srch.Node;
import srch.interfaces.IDirectionNode;

public class DependencyPathNode extends StepNode implements IDirectionNode {

	private Direction direction;
	private int dependency;
	private int dependencyCount;
	private int lastStep;

	public DependencyPathNode(Location initial, int dependency, int initialStep) 
	{
		super(initial, initialStep);
		
		this.direction 			= null;
		this.dependency 		= dependency;
		this.dependencyCount 	= 0;
		this.lastStep			= initialStep;
	}

	public DependencyPathNode(StepNode parent, Direction dir, Location loc) 
	{
		super(parent, loc);
		
		this.direction			= dir;
		this.dependency 		= ((DependencyPathNode) parent).dependency;
		this.dependencyCount 	= ((DependencyPathNode) parent).dependencyCount;
		this.lastStep			= ((DependencyPathNode) parent).lastStep;
		
		if (Planner.getInstance().getModel(lastStep).hasObject(dependency, loc)) 
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
			Location loc = this.getLocation().newLocation(dir);
			
			if (Planner.getInstance().getModel(lastStep).isFree(this.getObject(), loc))
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
		
		for (Node n = this; n.getParent() != null; n = n.getParent()) 
		{
			Location loc = n.getLocation();
			
			if (Planner.getInstance().getModel(lastStep).hasObject(dependency, loc))
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
