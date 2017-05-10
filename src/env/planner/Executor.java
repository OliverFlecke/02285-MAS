package env.planner;

import java.util.List;
import java.util.logging.Logger;

import env.model.CellModel;
import env.model.WorldModel;
import level.Location;
import level.action.Action;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import logging.LoggerFactory;
import srch.searches.PathfindingSearch;

public class Executor {
	
	private static final Logger logger = LoggerFactory.getLogger(Executor.class.getName());
	
	private Planner planner;
	
	public Executor(Planner planner)
	{
		this.planner = planner;
	}

	/**
	 * Moves the agent to a location next to the box
	 * @param agent to move next to the box
	 * @param box 
	 * @return True if the movement was possible
	 */
	protected boolean getAgentToBox(Agent agent, Box box)
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
		
		executeActions(initialStep, actions);
		
		return true;
	}
	
	/**
	 * Move an object to a location 
	 * @param agent which should move the object
	 * @param tracked The object to move
	 * @param location to move the the object to
	 * @return True if the movement is possible
	 */
	protected boolean getObjectToLocation(Agent agent, Cell tracked, Location location)
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

		executeActions(initialStep, actions);
		
		return true;
	}
	
	private void executeActions(int initialStep, List<Action> actions)
	{
		if (actions.isEmpty()) return;
		
		int step = initialStep, finalStep = initialStep + actions.size();
		
		int updateLimit = Math.min(finalStep + 1, planner.dataModelCount());
		
		// Update the grid models with the actions
		for (Action action : actions)
		{
			planner.getModel(step++).doExecute(action);

			for (int futureStep = step; futureStep < updateLimit; futureStep++)
			{
				planner.getModel(futureStep).doExecute(action);				
			}
		}
		
//		if (planner.dataModelCount() - finalStep > 3)
//		{
//			// These two models contain the update
//			GridWorldModel m0 = planner.getModel(finalStep    );
//			GridWorldModel m1 = planner.getModel(finalStep - 1);
//			// This model is not updated
//			GridWorldModel m2 = planner.getModel(finalStep - 2);
//			
//			GridWorldModel c1 = ModelUtil.compareModels(m0, m1);			
//			GridWorldModel c2 = ModelUtil.compareModels(m2, c1);
//			
//			GridWorldModel diff = ModelUtil.diffModels(c1, c2);
//		}
		
		for (int futureStep = finalStep + 1; futureStep < planner.dataModelCount(); futureStep++)
		{
			Location fr	= actions.get(0).getAgentLocation(),
					 to	= actions.get(actions.size() - 1).getNewAgentLocation();
			
			CellModel model = planner.getModel(futureStep);
			
			if (model != null) 	model.move(WorldModel.AGENT, fr, to);
			else 				logger.warning("model == null");				
		}
	}
}
