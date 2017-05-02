package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import level.Direction;
import srch.Node;

public class ClosestNode extends Node {
	
	public ClosestNode(Location initial, int initialStep) {
		super(initial, initialStep);
	}
	
	public ClosestNode(Location initial, int object, int initialStep) {
		super(initial, object, initialStep);
	}

	public ClosestNode(Node parent, Direction direction, Location location) {
		super(parent, direction, location);
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.DIRECTIONS.length);
		
		for (Direction dir : Direction.DIRECTIONS)
		{
			Location loc = Direction.newLocation(dir, this.getLocation());
			
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
