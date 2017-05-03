package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import level.Direction;
import srch.Node;
import srch.interfaces.IDirectionNode;

public class LocationNode extends Node implements IDirectionNode {
	
	private Direction direction;

	public LocationNode(Location initial, int initialStep) 
	{
		super(initial, initialStep);
		
		this.direction = null;
	}

	public LocationNode(Node parent, Direction direction, Location location) 
	{
		super(parent, location);
		
		this.direction = direction;
	}

	@Override
	public Direction getDirection() 
	{
		return direction;
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.EVERY.length);
		
		for (Direction dir : Direction.EVERY)
		{
			Location loc = Direction.newLocation(dir, this.getLocation());
			
			if (WorldModel.getInstance().isFree(this.getObject(), loc))
			{
				expandedNodes.add(new LocationNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Location> extractPlan() 
	{
		LinkedList<Location> plan = new LinkedList<Location>();
		
		for (Node n = this; n != null; n = n.getParent())
		{
			plan.addFirst(n.getLocation());
		}		
		return plan;
	}
}
