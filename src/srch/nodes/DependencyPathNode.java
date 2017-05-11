package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.DataModel;
import env.model.WorldModel;
import level.DependencyPath;
import level.Direction;
import level.Location;
import level.cell.Agent;
import srch.Node;
import srch.interfaces.IDirectionNode;
import util.ModelUtil;

public class DependencyPathNode extends Node implements IDirectionNode {

	private Direction 	direction;
	private Agent		agent;
	private int 		dependency;
	private int 		dependencyCount;
	private boolean 	ignoreLast;
	private DataModel 	model;

	public DependencyPathNode(Location initial, Agent agent, int dependency, boolean includeLast, DataModel model) 
	{
		super(initial);
		
		this.direction 			= null;
		this.agent				= agent;
		this.dependency 		= dependency;
		this.dependencyCount 	= 0;
		this.ignoreLast		= includeLast;
		this.model				= model;
	}

	public DependencyPathNode(Node parent, Direction dir, Location loc) 
	{
		super(parent, loc);
		
		DependencyPathNode n = (DependencyPathNode) parent;
		
		this.direction			= dir;
		this.agent				= n.agent;
		this.dependency 		= n.dependency;
		this.dependencyCount 	= n.dependencyCount;
		this.ignoreLast		= n.ignoreLast;
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
		
		int agNumber = ModelUtil.getAgentNumber(agent);
		
		for (Node n = this; n != null; n = n.getParent()) 
		{			
			Location loc = n.getLocation();
			
			if (model.hasObject(dependency, loc) &&
				// Do not add dependency if n is last and ignoreLast
				!(n == this && ignoreLast) && 
				// Do not add dependency if n is first
				!(n.getParent() == null) && 
				// Do not add dependency if dependency is box of agent's color
				!(model.hasObject(DataModel.BOX, loc) && model.getColor(loc).equals(agent.getColor())) && 
				// Do not add dependency if dependency is agent itself
				!(model.hasObject(agNumber, WorldModel.BOX_MASK, loc)))
			{
				path.addDependency(loc);
			}
			
			path.addToPath(loc);
		}
		return path;
	}

}
