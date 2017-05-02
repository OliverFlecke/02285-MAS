package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import level.Direction;
import srch.Node;

public class LocationNode extends Node {

	public LocationNode(Location initial, int initialStep) {
		super(initial, initialStep);
	}

	public LocationNode(Node parent, Direction direction, Location location) {
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
