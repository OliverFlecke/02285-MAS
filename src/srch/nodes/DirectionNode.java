package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.WorldModel;
import env.planner.Planner;
import jason.environment.grid.Location;
import level.Direction;
import srch.Node;

public class DirectionNode extends Node {

	public DirectionNode(Location initial, int initialStep) {
		super(initial, WorldModel.BOX, initialStep);
	}

	public DirectionNode(Node parent, Direction direction, Location location) {
		super(parent, direction, location);
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.DIRECTIONS.length);
		
		for (Direction dir : Direction.DIRECTIONS)
		{
			Location loc = Direction.newLocation(dir, this.getLocation());
			
			if (Planner.getModel(this.getStep()).isFree(this.getObject(), loc))
			{
				expandedNodes.add(new DirectionNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Direction> extractPlan() 
	{
		LinkedList<Direction> plan = new LinkedList<Direction>();
		
		for (Node n = this; n.getDirection() != null; n = n.getParent()) 
		{
			plan.addFirst(n.getDirection());
		}		
		return plan;
	}

}
