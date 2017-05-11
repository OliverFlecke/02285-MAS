package env.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import env.model.*;
import level.Color;
import level.DependencyPath;
import level.Location;
import level.action.Action;
import level.cell.*;
import logging.LoggerFactory;
import srch.searches.closest.AgentSearch;
import srch.searches.closest.StorageSearch;

public class Planner {
	
	private static final Logger logger = LoggerFactory.getLogger(Planner.class.getName());
	
	private static 	WorldModel 	worldModel;		
	private static 	Planner 	instance;

	private ArrayList<CellModel> 		dataModels;
	private ArrayList<ArrayList<Action>> 	actions;
	
	private Executor executor;
	
	public static Planner getInstance() {
		return instance;
	}

	/**
	 * @return The last model in the planner
	 */
	public CellModel getLastModel() {
		return getModel(getLastStep());
	}
	
	public int dataModelCount() {
		return dataModels.size();
	}
	
	/**
	 * Find a solution to the level
	 */
	public void plan()
	{
		logger.setLevel(Level.FINE);
		
		instance = this;
		
		worldModel = WorldModel.getInstance();
		
		SimulationModel.setPlanner(this);
		
		executor = new Executor(this);
		
		dataModels = new ArrayList<CellModel>(Arrays.asList(new CellModel(worldModel)));
		
		actions = new ArrayList<ArrayList<Action>>(worldModel.getNbAgs());
		
		for (int i = 0; i < worldModel.getNbAgs(); i++)
		{
			actions.add(new ArrayList<Action>());
		}
		
		for (Goal goal : Preprocessor.preprocess())
		{
			solveGoal(goal);
		}
	}
	
	private void solveGoal(Goal goal)
	{		
		Agent agent = goal.getBox().getAgent();
		
		if (!planAgentToBox(agent, goal.getBox()))
			throw new UnsupportedOperationException("Unable to get " + agent + " to " + goal.getBox());
		
		if (!planObjectToLocation(agent, goal.getBox(), goal.getLocation()))
			throw new UnsupportedOperationException("Unable to get " + goal.getBox() + " to " + goal);
	}
	
	private boolean planAgentToBox(Agent agent, Box box)
	{
		CellModel model = getModel(getInitialStep(agent));
		
		if (!planAgentToBox(agent, box, model))
		{
			model = getLastModel();
			
			return planAgentToBox(agent, box, model);
		}
		return true;
	}
	
	private boolean planObjectToLocation(Agent agent, Cell tracked, Location goal)
	{
		CellModel model = getModel(getInitialStep(agent));
		
		if (!planObjectToLocation(agent, tracked, goal, model))
		{
			model = getLastModel();
			
			return planObjectToLocation(agent, tracked, goal, model);
		}
		return true;
	}
	
	private boolean planObjectToLocation(Agent agent, Cell tracked, Location goal, CellModel model)
	{
		// TODO: We make a trajectory in one model and ask agents to solve dependencies in another
		// The trajectory should be made using the helping agent's model
		DependencyPath dependencyPath = DependencyPath.getDependencyPath(agent, tracked, goal, model);
		
		OverlayModel overlay = new OverlayModel(DataModel.IN_USE, dependencyPath.getPath());
		
		solveDependencies(agent.getColor(), dependencyPath.getDependencies(), overlay, model);	
		
		return executor.getObjectToLocation(agent, tracked, goal);
	}

	private boolean planAgentToBox(Agent agent, Box box, CellModel model) 
	{
		DependencyPath dependencyPath = DependencyPath.getDependencyPath(agent, box, model);
		
		OverlayModel overlay = new OverlayModel(DataModel.IN_USE, dependencyPath.getPath());
		
		solveDependencies(agent.getColor(), dependencyPath.getDependencies(), overlay, model);	
		
		return executor.getAgentToBox(agent, box);
	}
	
	private void solveDependencies(Color color, List<Location> dependencies, DataModel overlay, CellModel model)
	{
		for (Location dependency : dependencies)
		{
			if (model.hasObject(DataModel.BOX, dependency))
			{
				Box box = model.getBox(dependency);

				if (!box.getColor().equals(color))
				{							
					// TODO: Use agent model
					Agent otherAgent = model.getAgent(AgentSearch.search(box.getColor(), box.getLocation(), model));
					
					CellModel otherModel = getModel(getInitialStep(otherAgent));
					
					Location storage = StorageSearch.search(box.getLocation(), otherAgent, overlay, otherModel);

					planObjectToLocation(otherAgent, box, storage);
				}
			}
			else if (model.hasObject(DataModel.AGENT, dependency))
			{
				Agent otherAgent = model.getAgent(dependency);
				
				CellModel otherModel = getModel(getInitialStep(otherAgent));
				
				Location storage = StorageSearch.search(otherAgent.getLocation(), otherAgent, overlay, otherModel);
				
				if (storage == null)
					throw new UnsupportedOperationException("Unable to find storage");

				planObjectToLocation(otherAgent, otherAgent, storage, otherModel);
			}
		}
	}
	
	/**
	 * @return The unsolved goals of the last model in this planner
	 */
	public int countUnsolvedGoals()
	{
		return getLastModel().countUnsolvedGoals();
	}
	
	/**
	 * @param agent
	 * @return The initial step of the agent, which is the length of the agent's actions + 1
	 */
	public int getInitialStep(Agent agent)
	{
		return actions.get(agent.getNumber()).size();
	}
	
	/**
	 * @param step
	 * @param action
	 * @return True, if there is an agent with the opposite action in the step
	 */
	public boolean hasAgentWithOppositeAction(int step, Action action)
	{
		if (hasModel(step))
		{
			CellModel model = getModel(step);
			
			Agent agent = model.getAgent(action.getNewAgentLocation());
			
			if (agent != null && step < actions.get(agent.getNumber()).size())
			{
				return action.isOpposite(actions.get(agent.getNumber()).get(step));
			}
		}
		return false;
	}
	
	/**
	 * @param step to check
	 * @return True if the planner already has this step in its models
	 */
	public boolean hasModel(int step) {
		return step < dataModels.size();
	}
	
	public CellModel getModel(Agent agent) {
		return getModel(getInitialStep(agent));
	}
	
	/**
	 * @param step
	 * @return The data model with the given step
	 */
	public CellModel getModel(int step)
	{
		if (step > dataModels.size())
		{
			throw new UnsupportedOperationException("getModel - step is too large: " + step + " Size is only: " + dataModels.size());
		}
		
		// Should only trigger when step == gridModels.size()
		if (!hasModel(step))
		{
			dataModels.add(new CellModel(dataModels.get(step - 1)));
		}
		
		return dataModels.get(step);
	}
	
	public ArrayList<ArrayList<Action>> getActions()
	{
		return actions;
	}
	
	/**
	 * @return The length of the solution
	 */
	public int getLastStep()
	{
		return actions.stream().max((a, b) -> a.size() - b.size()).get().size();
	}
}

