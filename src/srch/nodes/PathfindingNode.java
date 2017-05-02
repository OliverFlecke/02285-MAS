package srch.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import env.model.GridWorldModel;
import env.model.SimulationWorldModel;
import env.model.WorldModel;
import env.planner.Planner;
import jason.environment.grid.Location;
import level.Direction;
import level.Actions.Action;
import level.Actions.SkipAction;
import level.cell.Agent;
import level.cell.Cell;
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
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Action.EVERY.length);
		
		for (Action action : Action.EVERY)
		{			
			if (Planner.getModel(this.getStep()).canExecute(action, localModel.getAgentLocation()));
			{
				expandedNodes.add(new PathfindingNode(this, action, localModel.run(action)));
			}
		}
		return expandedNodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Direction> extractPlan() 
	{		
		for (PathfindingNode n = this; n.getAction() != null; n = (PathfindingNode) n.getParent()) 
		{
			Planner.getModel(getStep()).doExecute(action, agLoc);
		}
		
	}

}
