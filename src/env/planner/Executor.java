package env.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import env.model.CellModel;
import env.model.DataModel;
import env.model.FutureModel;
import level.Location;
import level.action.Action;
import level.action.SkipAction;
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
			logger.info(agent + " could not find path to box " + box.getLetter());
			return false;			
		}

		logger.info(agent + " to " + box + ":\t\t" + actions.toString());

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

		logger.info(tracked + " to " + location + ":\t\t" + actions.toString());
		
		planner.getActions().get(agent.getNumber()).addAll(actions);

		executeActions(initialStep, actions);
		
		return true;
	}
	
	public void executeSkips(Agent agent, int steps)
	{
		int initialStep = planner.getInitialStep(agent);
		
		List<Action> actions = new ArrayList<Action>();
		
		for (int i = 0; i < steps; i++)
		{
			actions.add(new SkipAction(agent.getLocation()));
		}
		
		planner.getActions().get(agent.getNumber()).addAll(actions);
		
		executeActions(initialStep, actions);

		logger.info(agent + " skipping: " + actions.size() + " times");
	}
	
	private void executeActions(int initialStep, List<Action> actions)
	{
		if (actions.isEmpty()) return;
		
		FutureModel futureModel = new FutureModel(planner.getModel(initialStep));
		
		int step = initialStep, finalStep = initialStep + actions.size() + 1;
		
		int updateLimit = Math.min(finalStep, planner.dataModelCount());
		
		// Update the grid models with the actions
		for (Action action : actions)
		{
			futureModel.doExecute(action);
			planner.getModel(++step).doExecute(action);

			for (int futureStep = step + 1; futureStep < updateLimit; futureStep++)
			{
				planner.getModel(futureStep).doExecute(action);				
			}
		}
		
		Map<Cell, Location> originalLocations = futureModel.getOriginalLocations();
		
		for (int futureStep = finalStep; futureStep < planner.dataModelCount(); futureStep++)
		{
			CellModel model = planner.getModel(futureStep);
			
			for (Entry<Cell, Location> entry : originalLocations.entrySet())
			{
				int objectType = entry.getKey() instanceof Agent ? DataModel.AGENT : DataModel.BOX;
				
				model.move(objectType, entry.getValue(), ((Cell) entry.getKey()).getLocation());
			}
		}
	}
}
