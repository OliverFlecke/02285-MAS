package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.WorldModel;
import env.planner.Planner;
import jason.environment.grid.Location;
import level.Direction;
import level.Actions.Action;
import level.Actions.SkipAction;
import srch.Node;
import srch.interfaces.IActionNode;

public class PathfindingNode extends StepNode implements IActionNode {

	private Action action;
	
	public PathfindingNode(Location initial, int initialStep) 
	{
		super(initial, initialStep);
		
		this.action = null;
	}

	public PathfindingNode(StepNode parent, Action action, Location location) 
	{
		super(parent, location);
		
		this.action = action;
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.DIRECTIONS.length);
		
		expandedNodes.add(new PathfindingNode(this, new SkipAction(), this.getLocation()));
		
		for (Direction dir : Direction.DIRECTIONS)
		{
			
//			if (Planner.getModel(this.getStep()).isFree(this.getObject(), loc))
//			{
//				expandedNodes.add(new PathfindingNode(this, dir, loc));
//			}
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

	@Override
	public Action getAction() 
	{
		return action;
	}

	@Override
	public <T> T extractPlan() {
		// TODO Auto-generated method stub
		return null;
	}

}
