package env.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import env.model.CellModel;
import env.model.DataModel;
import env.model.OverlayModel;
import env.model.SimulationModel;
import env.model.WorldModel;
import level.DependencyPath;
import level.Location;
import level.action.Action;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import level.cell.Goal;
import logging.LoggerFactory;
import srch.searches.closest.AgentSearch;
import srch.searches.closest.StorageSearch;

public class Planner {
	
	private static final Logger logger = LoggerFactory.getLogger(Planner.class.getName());
	
	private static 	WorldModel 	worldModel;		
	private static 	Planner 	instance;

	private ArrayList<CellModel> 			dataModels;
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
		
		solveLevel(Preprocessor.preprocess(worldModel));
	}
	
	private void solveLevel(List<Goal> goals)
	{
		do
		{
			for (Goal goal : goals)
			{
				solveGoal(goal);
			}
			
			goals = Preprocessor.preprocess(getLastModel());
		}
		while (!goals.isEmpty());
	}
	
	private void solveGoal(Goal goal)
	{		
		Box	  		box		= goal.getBox();
		Agent 		agent 	= box.getAgent();
		Location 	loc 	= goal.getLocation();

		planAgentToBox(agent, box, new OverlayModel());
		planObjectToLocation(agent, box, loc, new OverlayModel());
	}

	private boolean planAgentToBox(Agent agent, Box box, OverlayModel previousOverlay) 
	{
		int				step			= getInitialStep(agent);
		CellModel 		model 			= getModel(step);
		DependencyPath 	dependencyPath 	= DependencyPath.getDependencyPath(agent, box, step);
		OverlayModel	overlay			= new OverlayModel(previousOverlay, dependencyPath.getPath());
		
		if (dependencyPath.hasDependencies())
		{
			Entry<Location, Integer> dependency = dependencyPath.getDependency(agent.getLocation());
			
			if (step < dependency.getValue()) 
			{
				executor.executeSkips(agent, dependency.getValue() - step);
				return planAgentToBox(agent, box, previousOverlay);
			}
			
			int newStep = solveDependency(dependency.getKey(), overlay, step);
			
			if (newStep > getInitialStep(agent)) 
			{
				// Maximum: newStep - step, 1 for optimal solution
//				executor.executeSkips(agent, newStep - step);
				executor.executeSkips(agent, 1);
			}
			return planAgentToBox(agent, box, previousOverlay);
		}
		return executor.getAgentToBox(agent, box);
	}

	private boolean planObjectToLocation(Agent agent, Cell tracked, Location loc, OverlayModel previousOverlay) 
	{
		int				step			= getInitialStep(agent);
		CellModel 		model 			= getModel(step);
		DependencyPath 	dependencyPath 	= DependencyPath.getDependencyPath(agent, tracked, loc, step);
		OverlayModel	overlay			= new OverlayModel(previousOverlay, dependencyPath.getPath());
		
		if (dependencyPath.hasDependencies())
		{
			Entry<Location, Integer> dependency = dependencyPath.getDependency(tracked.getLocation());
			
			if (step < dependency.getValue()) 
			{
				executor.executeSkips(agent, dependency.getValue() - step);
				return planObjectToLocation(agent, tracked, loc, previousOverlay);
			}
			
			int newStep = solveDependency(dependency.getKey(), overlay, step);
			
			if (newStep > getInitialStep(agent)) 
			{
				// Maximum: newStep - step, 1 for optimal solution
//				executor.executeSkips(agent, newStep - step);
				executor.executeSkips(agent, 1);
			}
			return planObjectToLocation(agent, tracked, loc, previousOverlay);
		}		
		return executor.getObjectToLocation(agent, tracked, loc);
	}
	
	private int solveDependency(Location dependency, OverlayModel overlay, int step)
	{
		CellModel model = getModel(step);
		
		if (model.hasObject(DataModel.BOX, dependency))
		{
			Box 	box 	= model.getBox(dependency);			
			Agent 	agent 	= model.getAgent(AgentSearch.search(box.getColor(), box.getLocation(), model));
			
			return solveAgentToBoxDependency(agent, box, overlay, step);
		}
		else if (model.hasObject(DataModel.AGENT, dependency))
		{
			Agent	agent 	= model.getAgent(dependency);
			
			return solveObjectToLocationDependency(agent, agent, overlay, step);
		}		
		throw new UnsupportedOperationException("Attempt to solve unknown dependency");
	}

	private int solveAgentToBoxDependency(Agent agent, Box box, OverlayModel overlay, int step) 
	{
		int agentStep = getInitialStep(agent);
		
		if (step < agentStep)
		{
			return agentStep;
		}
		
		planAgentToBox(agent, box, overlay);
		
		return solveObjectToLocationDependency(agent, box, overlay, agentStep);
	}
	
	private int solveObjectToLocationDependency(Agent agent, Cell tracked, OverlayModel overlay, int step) 
	{
		int agentStep = getInitialStep(agent);
		
		if (step < agentStep)
		{
			return agentStep;
		}
		
		CellModel model = getModel(agentStep);
		
		Location storage = StorageSearch.search(agent.getLocation(), agent, overlay, model);
		
		planObjectToLocation(agent, tracked, storage, overlay);
		
		return -1;
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

