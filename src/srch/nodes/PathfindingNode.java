package srch.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import env.model.SimulationWorldModel;
import env.planner.Planner;
import level.Location;
import level.action.Action;
import level.action.Action.ActionType;
import level.cell.Agent;
import level.cell.Cell;
import srch.Node;
import srch.interfaces.IActionNode;

public class PathfindingNode extends Node implements IActionNode {

	private Action action;
	private SimulationWorldModel model;
	private static Planner planner;
	private boolean plannerHasFailed = false;
	private int skipCount;
	
	public PathfindingNode(Agent agent, Cell tracked, int initialStep, Planner plan) 
	{
		super(agent.getLocation());

		planner = plan;
		skipCount = 0;
		this.action 	= null;
		this.model 		= new SimulationWorldModel(planner.getModel(initialStep), initialStep, agent, tracked);
	}

	public PathfindingNode(Node parent, Action action, SimulationWorldModel model) 
	{
		super(parent, model.getAgentLocation());

		skipCount = ((PathfindingNode) parent).skipCount;
		
		if (action.getType() == ActionType.SKIP) skipCount++;
		
		this.action 	= action;
		this.model 		= model;		
	}

	@Override
	public Action getAction() 
	{
		return action;
	}
	
	public Location getTrackedLoc()
	{
		return model.getTrackedLocation();
	}
	
	public int getSkipCount()
	{
		return skipCount;
	}
	
	public SimulationWorldModel getModel() 
	{
		return model;
	}

	@Override
	public List<Node> getExpandedNodes()
	{		
		if (this.action != null && this.action.getType() == ActionType.SKIP && ((PathfindingNode) this.getParent()).plannerHasFailed)
		{
			return Collections.emptyList();
		}
			
		List<Node> expandedNodes = new ArrayList<Node>();
		
		for (Action action : Action.Every(model.getAgentLocation()))
		{			
			if (model.canExecute(action))
			{
				expandedNodes.add(new PathfindingNode(this, action, model.run(action)));
			}
			else
			{
				plannerHasFailed = model.hasFailed();
			}
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

}
