package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.SimulationWorldModel;
import level.Location;
import level.action.Action;
import level.action.Action.ActionType;
import level.action.SkipAction;
import level.cell.Cell;
import srch.Node;
import srch.interfaces.IActionNode;

public class PathfindingNode extends Node implements IActionNode {

	private Action action;
	private SimulationWorldModel model;
	
	public PathfindingNode(Cell agent, Cell tracked, int initialStep) 
	{
		super(agent.getLocation());

		action 	= null;
		model 	= new SimulationWorldModel(initialStep, tracked);
	}

	public PathfindingNode(Node parent, Action action, SimulationWorldModel model) 
	{
		super(parent, action.getNewAgentLocation());
		
		this.action = action;
		this.model 	= model;
	}

	@Override
	public Action getAction() 
	{
		return action;
	}
	
	public boolean isSkipNode()
	{
		if (action == null) return true;
		return action.getType() == ActionType.SKIP;
	}
	
	public Location getTrackedLoc()
	{
		return model.getTrackedLocation();
	}
	
	public SimulationWorldModel getModel() 
	{
		return model;
	}

	@Override
	public List<Node> getExpandedNodes()
	{			
		List<Node> expandedNodes = new ArrayList<Node>();
		
		for (Action action : Action.Every(this.getLocation(), this.getAction()))
		{			
			if (model.canExecute(action))
			{
				expandedNodes.add(new PathfindingNode(this, action, model.run(action)));
			}
		}
		if (this.isSkipNode())
		{
			Action action = new SkipAction(this.getLocation());
			
			expandedNodes.add(new PathfindingNode(this, action, model.run(action)));
		}
		return expandedNodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Action> extractPlan() 
	{		
		LinkedList<Action> plan = new LinkedList<>();
		
		for (PathfindingNode n = this; n.getAction() != null; n = (PathfindingNode) n.getParent()) 
		{
			plan.addFirst(n.action);
		}
		return plan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathfindingNode other = (PathfindingNode) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		return true;
	}
	
	@Override
	public String toString() 
	{
		return this.getAction().toString() + " - " + super.toString();
	}

}
