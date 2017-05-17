package env.planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import env.model.CellModel;
import env.model.DataModel;
import env.model.FutureModel;
import level.Location;
import level.action.Action;
import level.action.PullAction;
import level.action.PushAction;
import level.action.SkipAction;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import level.cell.Colored;
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
		
		executeActions(agent, initialStep, actions);
		
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

		executeActions(agent, initialStep, actions);
		
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
		
		executeActions(agent, initialStep, actions);

		logger.info(agent + " skipping: " + actions.size() + " times");
	}
	
	private void executeActions(Agent agent, int initialStep, List<Action> actions)
	{
		if (actions.isEmpty()) return;
		
		FutureModel futureModel = new FutureModel(planner.getModel(initialStep));
		
		int step = initialStep;
		
		// Create models for the actions
		for (int modelCount = initialStep + 1; modelCount < initialStep + actions.size() + 1; modelCount++)
		{
			planner.getModel(modelCount);
		}
		
		// Update the grid models with the actions
		for (Action action : actions)
		{
			futureModel.doExecute(action);
			
			updateFuture(futureModel, ++step);
		}
		
		for (int futureStep = ++step; futureStep < planner.dataModelCount(); futureStep++)
		{
			updateFuture(futureModel, futureStep);
		}

		for (int modelStep = initialStep + 1; modelStep < planner.dataModelCount(); modelStep++)
		{
			addLock(agent, modelStep);
		}
	}
	
	private void updateFuture(FutureModel futureModel, int step)
	{		
		Map<Cell, Location> originalLocations = futureModel.getOriginalLocations();
		
		CellModel model = planner.getModel(step);
		
		Map<Cell, Cell> objectReferences = new HashMap<>();
		
		// Remove old cells and store object references
		for (Entry<Cell, Location> entry : originalLocations.entrySet())
		{
			int objectType = entry.getKey() instanceof Agent ? DataModel.AGENT : DataModel.BOX;
			
			Cell object = model.removeCell(objectType, entry.getValue());
			
			objectReferences.put(entry.getKey(), object);
		}
		
		// Add object references
		for (Entry<Cell, Cell> entry : objectReferences.entrySet())
		{
			model.addCell((Colored) entry.getKey(), entry.getValue());
		}
	}
	
	private void addLock(Agent agent, int step)
	{
		DataModel model = planner.getModel(step);
		
		for (int actionStep = step - 1; actionStep < planner.getActions().get(agent.getNumber()).size(); actionStep++)
		{
			Action action = planner.getActions().get(agent.getNumber()).get(actionStep);
					
			model.add(DataModel.LOCKED, action.getNewAgentLocation());
			
			if (action instanceof PullAction)
			{
				model.add(DataModel.LOCKED, ((PullAction) action).getNewBoxLocation());
			}
			else if (action instanceof PushAction)
			{
				model.add(DataModel.LOCKED, ((PushAction) action).getNewBoxLocation());
			}
		}
	}
}
