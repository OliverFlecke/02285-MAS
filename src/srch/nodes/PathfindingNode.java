package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.GridWorldModel;
import env.model.SimulationWorldModel;
import env.planner.Planner;
import level.Location;
import level.action.Action;
import level.cell.Cell;
import srch.Node;
import srch.interfaces.IActionNode;

public class PathfindingNode extends StepNode implements IActionNode {

	private Action action;
	private SimulationWorldModel model;
	private static Planner planner;
	
	public PathfindingNode(GridWorldModel model, Cell agent, Cell tracked, int initialStep, Planner plan) 
	{
		super(agent.getLocation(), initialStep);
		
		planner = plan;
		this.action 	= null;
		this.model 		= new SimulationWorldModel(model, agent, tracked);
	}

	public PathfindingNode(StepNode parent, Action action, SimulationWorldModel model) 
	{
		super(parent, null);

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
	
	public SimulationWorldModel getModel() 
	{
		return model;
	}

	@Override
	public List<Node> getExpandedNodes()
	{		
		List<Node> expandedNodes = new ArrayList<Node>();
		
		for (Action action : Action.Every(model.getAgentLocation()))
		{			
			if (model.canExecute(action) && planner.getModel(getStep()).canExecute(action))
			{
				expandedNodes.add(new PathfindingNode(this, action, model.run(action)));
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
