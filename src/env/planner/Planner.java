package env.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import env.model.DataWorldModel;
import env.model.GridWorldModel;
import env.model.SimulationWorldModel;
import env.model.WorldModel;
import level.DependencyPath;
import level.Location;
import level.action.Action;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import level.cell.Goal;
import srch.searches.DependencySearch;
import srch.searches.PathfindingSearch;
import srch.searches.closest.AgentSearch;
import srch.searches.closest.BoxSearch;
import srch.searches.closest.StorageSearch;

public class Planner {
	
	private static final Logger logger = Logger.getLogger(Planner.class.getName());
	
	private static WorldModel 	worldModel;	
	
	public static Planner instance;

	private ArrayList<DataWorldModel> gridModels;
	
	public ArrayList<ArrayList<Action>> actions;
	
	public static Planner getInstance()
	{
		return instance;
	}
	
	/**
	 * Find a solution to the level
	 */
	public void plan()
	{
//		logger.setLevel(Level.FINE);

		
		instance = this;
		
		SimulationWorldModel.setPlanner(this);
		
		worldModel = WorldModel.getInstance();
		
		gridModels = new ArrayList<DataWorldModel>();
		
		gridModels.add(new DataWorldModel(worldModel));
		
		actions = new ArrayList<ArrayList<Action>>(worldModel.getNbAgs());
		
		for (int i = 0; i < worldModel.getNbAgs(); i++)
		{
			actions.add(new ArrayList<Action>());
		}
		
//		matchBoxesAndGoals();
//		
//		Queue<Agent> queue = new LinkedList<Agent>();
//		for (Agent agent : worldModel.getAgents()) 
//			queue.add(agent);
		
//		List<Goal> goals = prioritizeGoals(goals);
		
		List<Goal> goals = preprocessLevel();
		
//		for (Collection<Goal> goals; !(goals = getUnsolvedGoals()).isEmpty();)
		for (Goal goal : goals)
		{
			Agent agent = goal.getBox().getAgent();
//			Agent agent = queue.poll();
			
//			Collection<Goal> solvableGoals = getSolvableGoals(goals, agent);
			
//			for (Goal goal : solvableGoals)
			{
				solveDependencies(agent, goal);				

				if (getAgentToBox(goal.getBox(), agent))
				{
					getObjectToLocation(goal.getBox(), goal.getLocation(), agent);
//					break;
				}
			}
			
//			queue.add(agent);
		}
	}

	/**
	 * Moves the agent to a location next to the box
	 * @param box 
	 * @param agent to move next to the box
	 * @return True if the movement was possible
	 */
	public boolean getAgentToBox(Box box, Agent agent)
	{
		int initialStep = getInitialStep(agent);

		List<Action> actions = PathfindingSearch.search(agent, agent, box.getLocation(), 1, initialStep);

		if (actions == null)
		{
			logger.info(agent.getName() + " could not find path to box " + box.getLetter());
			return false;			
		}

		logger.fine("Path from agent to box:\n\t" + actions.toString());

		this.actions.get(agent.getNumber()).addAll(actions);

		int step = initialStep;
		// Update the grid models with the actions
		for (Action action : actions)
		{
			getModel(step++).doExecute(action);
		
			// If there are future models, update these with the action
			if (step < gridModels.size())
			{				
				for (int futureStep = step; futureStep < gridModels.size(); futureStep++)
				{
					getModel(futureStep).doExecute(action);				
				}
			}
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
		int initialStep = getInitialStep(agent);
		
		List<Action> actions = PathfindingSearch.search(agent, tracked, location, 0, initialStep);

		if (actions == null)
		{
			logger.info(agent.getName() + " could not find path to location " + location);
			return false;			
		}

		logger.info("Path from object to location:\n\t" + actions.toString());
		
		this.actions.get(agent.getNumber()).addAll(actions);

		int step = initialStep;
		// Update the grid models with the actions
		for (Action action : actions)
		{
			getModel(step++).doExecute(action);
		
			// If there are future models, update these with the action
			if (step < gridModels.size())
			{
				for (int futureStep = step; futureStep < gridModels.size(); futureStep++)
				{
					getModel(futureStep).doExecute(action);				
				}
			}
		}
		return true;
	}

	/**
	 * Solve dependencies for a given agent and goal
	 * @param agent
	 * @param goal
	 */
	private void solveDependencies(Agent agent, Goal goal) 
	{
		DependencyPath path = DependencyPath.getLocationDependencyPath(agent, goal.getBox().getLocation(), agent.getLocation());
		
		path.addDependencyPath(DependencyPath.getGoalDependencyPath(agent, goal));
		
		solveDependency(agent, path);
	}
	
	/**
	 * Solve a dependency path with a given agent
	 * @param agent
	 * @param dependencyPath
	 */
	private void solveDependency(Agent agent, DependencyPath dependencyPath)
	{
		if (dependencyPath.getDependencies().isEmpty()) return;
		
		DataWorldModel model = getModel(getInitialStep(agent));
		
		for (Location path : dependencyPath.getPath())
		{
			model.add(GridWorldModel.IN_USE, path);
		}

		for (Location dependency : dependencyPath.getDependencies())
		{
			if (model.hasObject(GridWorldModel.BOX, dependency))
			{
				Box box = model.getBox(dependency);

				if (!box.getColor().equals(agent.getColor()))
				{							
					Location storage = StorageSearch.search(box.getLocation(), model);
					Agent otherAgent = model.getAgent(AgentSearch.search(box.getColor(), box.getLocation()));

					DependencyPath otherDependencyPath = DependencyPath.getLocationDependencyPath(otherAgent, box.getLocation(), otherAgent.getLocation());
					otherDependencyPath.addDependencyPath(DependencyPath.getLocationDependencyPath(otherAgent, storage, box.getLocation()));
					solveDependency(otherAgent, otherDependencyPath);
					
					if (!getAgentToBox(box, otherAgent)) 
						throw new UnsupportedOperationException("Unable to get " + otherAgent.getName() + " to box " + box.getLetter());
					
					if (!getObjectToLocation(box, storage, otherAgent))
						throw new UnsupportedOperationException("Unable to get box " + box.getLetter() + " to location " + storage.toString());
				}
			}
			else if (model.hasObject(GridWorldModel.AGENT, dependency))
			{
				Agent otherAgent = model.getAgent(dependency);
				Location storage = StorageSearch.search(otherAgent.getLocation(), model);
				
				if (storage == null)
					throw new UnsupportedOperationException("Unable to find storage");
				
				solveDependency(otherAgent, DependencyPath.getLocationDependencyPath(otherAgent, storage, otherAgent.getLocation()));
				
				if (!getObjectToLocation(otherAgent, storage, otherAgent))
					throw new UnsupportedOperationException("Unable to get " + agent.getName() + " to location " + storage.toString());
			}
		}

		for (Location path : dependencyPath.getPath())
		{
			model.remove(GridWorldModel.IN_USE, path);
		}
	}
	
	/**
	 * @return The unsolved goals of the last model in this planner
	 */
	public Collection<Goal> getUnsolvedGoals()
	{
		return getModel(gridModels.size() - 1).getUnsolvedGoals();
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
			DataWorldModel model = getModel(step);
			
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
	public boolean hasModel(int step)
	{
		return step < gridModels.size();
	}
	
	/**
	 * @param step
	 * @return The data model with the given step
	 */
	public DataWorldModel getModel(int step)
	{
		if (step > gridModels.size())
		{
			throw new UnsupportedOperationException("getModel - step is too large: " + step + " Size is only: " + gridModels.size());
		}
		
		if (!hasModel(step))
		{
			gridModels.add(new DataWorldModel(gridModels.get(step - 1)));
		}
		
		return gridModels.get(step);
	}
	
	public ArrayList<ArrayList<Action>> getActions()
	{
		return actions;
	}
	
	private List<Goal> preprocessLevel()
	{
		matchBoxesAndGoals();
		
		matchAgentsAndBoxes();
		
		return calculateDependencies();
	}
	
	private void matchBoxesAndGoals()
	{		
		Set<Box> availableBoxes = new HashSet<>(worldModel.getBoxes());
		
		for (Goal goal : worldModel.getGoals())
		{
			Location boxLoc = BoxSearch.search(availableBoxes, goal.getLetter(), goal.getLocation());
			
			Box box = worldModel.getBox(boxLoc);

			if (box != null && availableBoxes.remove(box))
			{
				goal.setBox(box);
				box.setGoal(goal);
			}
			else logger.warning("ERROR: matchBoxesAndGoals()");
		}
	}
	
	private void matchAgentsAndBoxes()
	{
		Set<Box> boxes = worldModel.getGoals().stream()
							.map(goal -> goal.getBox())
							.collect(Collectors.toSet());
		
		for (Box box : boxes)
		{
			Location agLoc = AgentSearch.search(box.getColor(), box.getLocation());
			
			Agent agent = worldModel.getAgent(agLoc);
			
			if (agent != null)
			{
				box.setAgent(agent);
			} 
			else logger.warning("ERROR: matchAgentsAndBoxes()");
		}
	}
	
	private List<Goal> calculateDependencies()
	{
		Set<Goal> goals = worldModel.getGoals();
		
		Map<Goal, Set<Goal>> dependencies = new HashMap<>();
		
		initMap(dependencies, goals);
		
		for (Goal goal : goals)
		{			
			Box 	box 	= goal.getBox();
			Agent 	agent 	= box.getAgent();

	        DependencySearch.search(goal.getLocation(), box.getLocation(), WorldModel.BOX | WorldModel.GOAL)
	        	.stream().forEach(loc -> addDependency(dependencies, loc, goal));
	        
	        DependencySearch.search(box.getLocation(), agent.getLocation(), WorldModel.BOX | WorldModel.GOAL)
		        .stream().forEach(loc -> addDependency(dependencies, loc, goal));	        
		}
		return dependencies.entrySet().stream()
		        .sorted(Comparator.comparingInt(e -> e.getValue().size()))
		        .map(Map.Entry::getKey)
		        .collect(Collectors.toList());
	}
	
	private void addDependency(Map<Goal, Set<Goal>> dependencies, Location l, Goal goal)
	{
    	if (worldModel.hasObject(WorldModel.GOAL, l))
    	{
    		addToMap(dependencies, worldModel.getGoal(l), goal);
    	}
    	else
    	{
    		addToMap(dependencies, worldModel.getBox(l).getGoal(), goal);
    	}
	}

	private Collection<Goal> getSolvableGoals(Collection<Goal> goals, Agent agent) 
	{
		Map<Goal, Set<Goal>> goalDependencies = new HashMap<>();
		
		for (Goal goal : goals)
		{
			if (!goalDependencies.containsKey(goal))
			{
				goalDependencies.put(goal, new HashSet<Goal>());
			}
			
			Location from = goal.getLocation();
			Location to   = goal.getBox().getLocation();

	        List<Location> locations = DependencySearch.search(from, to, DataWorldModel.GOAL);
	        
	        locations.stream().forEach(loc -> addToMap(goalDependencies, worldModel.getGoal(loc), goal));
		}
		
		return goalDependencies.entrySet().stream()
//		        .sorted(Comparator.comparingInt(e -> e.getValue().size()))
				.filter(e -> e.getValue().isEmpty() && e.getKey().getBox().getColor().equals(agent.getColor()))
		        .map(Map.Entry::getKey)
//		        .sorted((g1, g2) -> d(g1, agent) - d(g2, agent))
		        .collect(Collectors.toList());
	}
	
	private static <K, V> void initMap(Map<K, Set<V>> map, Set<K> keys)
	{
		for (K key : keys)
		{
			map.put(key, new HashSet<V>());
		}
	}
	
	private static <K, V> void addToMap(Map<K, Set<V>> map, K key, V value)
	{
		if (map.containsKey(key))
		{
			map.get(key).add(value);
		}
		else
		{
			map.put(key, new HashSet<V>(Arrays.asList(value)));
		}
	}
	
	/**
	 * @return The length of the solution
	 */
	public int getSolutionLength()
	{
		return actions.stream().max((a, b) -> a.size() - b.size()).get().size();
	}
}

