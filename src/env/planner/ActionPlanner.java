package env.planner;

import java.util.List;
import java.util.logging.Logger;

import env.model.DataWorldModel;
import env.model.WorldModel;
import level.Location;
import level.action.Action;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import logging.LoggerFactory;
import srch.searches.PathfindingSearch;

public class ActionPlanner {
	
	private static final Logger logger = LoggerFactory.getLogger(ActionPlanner.class.getName());
	
	private Planner planner;
	
	public ActionPlanner(Planner planner)
	{
		this.planner = planner;
	}

	/**
	 * Moves the agent to a location next to the box
	 * @param box 
	 * @param agent to move next to the box
	 * @return True if the movement was possible
	 */
	public boolean getAgentToBox(Box box, Agent agent)
	{
		int initialStep = planner.getInitialStep(agent);

		List<Action> actions = PathfindingSearch.search(agent, agent, box.getLocation(), 1, initialStep);

		if (actions == null)
		{
			logger.info(agent.getName() + " could not find path to box " + box.getLetter());
			return false;			
		}

		logger.info("Agent to box:\t\t" + actions.toString());

		planner.getActions().get(agent.getNumber()).addAll(actions);

		int step = initialStep, lastActionStep = initialStep + actions.size();
		
		// Update the grid models with the actions
		for (Action action : actions)
		{
			planner.getModel(step++).doExecute(action);
		
			// If there are future models, update these with the action
			if (step < planner.dataModelCount())
			{				
				for (int futureStep = step; futureStep < lastActionStep; futureStep++)
				{
					planner.getModel(futureStep).doExecute(action);				
				}
			}
		}
		
		for (int futureStep = lastActionStep; futureStep < planner.dataModelCount(); futureStep++)
		{
			Location fr	= actions.get(0).getAgentLocation(),
					 to	= actions.get(actions.size() - 1).getNewAgentLocation();
			
			DataWorldModel model = planner.getModel(futureStep);
			
			if (model != null) 	model.move(WorldModel.AGENT, fr, to);
			else 				logger.warning("model == null");				
		}
		
		return true;
	}
	
	/**
	 * Move an object to a location 
	 * @param tracked The object to move
	 * @param location to move the the object to
	 * @param agent which should move the object
	 * @return True if the movement is possible
	 */
	public boolean getObjectToLocation(Cell tracked, Location location, Agent agent)
	{
		int initialStep = planner.getInitialStep(agent);
		
		List<Action> actions = PathfindingSearch.search(agent, tracked, location, 0, initialStep);

		if (actions == null)
		{
			logger.info(agent.getName() + " could not find path to location " + location);
			return false;			
		}

		logger.info("Object to location:\t" + actions.toString());
		
		planner.getActions().get(agent.getNumber()).addAll(actions);

		int step = initialStep;
		// Update the grid models with the actions
		for (Action action : actions)
		{
			planner.getModel(step++).doExecute(action);
		
			// If there are future models, update these with the action
			if (step < planner.dataModelCount())
			{
				for (int futureStep = step; futureStep < planner.dataModelCount(); futureStep++)
				{
					planner.getModel(futureStep).doExecute(action);				
				}
			}
		}
		return true;
	}
}
