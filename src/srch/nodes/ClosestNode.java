package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.WorldModel;
import level.Location;
import level.Direction;
import srch.Node;
import srch.interfaces.IDirectionNode;

public class ClosestNode extends Node implements IDirectionNode {
	
	private Direction direction;
	
	public ClosestNode(Location initial) {
		super(initial);
		this.direction = null;
	}
	
	public ClosestNode(Location initial, int object) {
		super(initial, object);
		this.direction = null;
	}

	public ClosestNode(Node parent, Direction direction, Location location) {
		super(parent, location);
		this.direction = direction;
	}

	@Override
	public Direction getDirection() {
		return this.direction;
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
				expandedNodes.add(new ClosestNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Location extractPlan() 
	{
		return this.getLocation();
	}

}
