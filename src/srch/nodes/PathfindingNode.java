package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.GridWorldModel;
import env.model.SimulationWorldModel;
import env.planner.Planner;
import jason.environment.grid.Location;
import level.Actions.Action;
import level.cell.Agent;
import srch.Node;
import srch.interfaces.IActionNode;

public class PathfindingNode extends StepNode implements IActionNode {

	private Action action;
	private SimulationWorldModel localModel;
	
	public PathfindingNode(Location initial, int initialStep, Agent agent, GridWorldModel model) 
	{
		super(initial, initialStep);
		
		this.localModel = new SimulationWorldModel(model, agent);
		this.action = null;
	}

	public PathfindingNode(StepNode parent, Action action, SimulationWorldModel model) 
	{
		super(parent, model.getCellLocation());
		
		this.localModel = model;
		
		this.action = action;
	}

	@Override
	public Action getAction() 
	{
		return action;
	}

	@Override
	public List<Node> getExpandedNodes()
	{		
		List<Node> expandedNodes = new ArrayList<Node>();
		
		for (Action action : Action.Every(localModel.getAgentLocation()))
		{			
			if (Planner.getModel(this.getStep()).canExecute(action));
			{
				expandedNodes.add(new PathfindingNode(this, action, localModel.run(action)));
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
			plan.addFirst(action);
		}
		return plan;
	}

}
