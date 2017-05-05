package env.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
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
import srch.searches.DependencyPathSearch;
import srch.searches.DependencySearch;
import srch.searches.PathfindingSearch;
import srch.searches.closest.AgentSearch;
import srch.searches.closest.StorageSearch;

public class Planner {
	
	private static final Logger logger = Logger.getLogger(Planner.class.getName());
	
	private static WorldModel 	worldModel;	

	private ArrayList<DataWorldModel> gridModels;
	
	public ArrayList<ArrayList<Action>> actions;
	
	public void plan()
	{
		SimulationWorldModel.setPlanner(this);
		
		worldModel = WorldModel.getInstance();
		
		gridModels = new ArrayList<DataWorldModel>();
		
		gridModels.add(new DataWorldModel(worldModel));
		
		actions = new ArrayList<ArrayList<Action>>(worldModel.getNbAgs());
		
		for (int i = 0; i < worldModel.getNbAgs(); i++)
		{
			actions.add(new ArrayList<Action>());
		}
		
		matchBoxesAndGoals();
		
		Queue<Agent> queue = new LinkedList<Agent>();
		for (Agent agent : worldModel.getAgents()) 
			queue.add(agent);
		
//		List<Goal> goals = prioritizeGoals(goals);
		
		
		for (Collection<Goal> goals; !(goals = getUnsolvedGoals()).isEmpty();)
		{
			Agent agent = queue.poll();
			
			DataWorldModel model = getModel(getInitialStep(agent));
			
			Collection<Goal> solvableGoals = getSolvableGoals(goals, agent);
			
			for (Goal goal : solvableGoals)
			{
				DependencyPath dependencyPath = getBoxDependencyPath(agent, goal.getBox());
				
				dependencyPath.addDependencyPath(getGoalDependencyPath(agent, goal));
				
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
							
							getAgentToBox(box, otherAgent);
							getObjectToLocation(box, storage, otherAgent);
						}
					}
					else if (model.hasObject(GridWorldModel.AGENT, dependency))
					{
						Agent otherAgent = model.getAgent(dependency);

						Location storage = StorageSearch.search(agent.getLocation(), model);
						
						getObjectToLocation(otherAgent, storage, otherAgent);
					}
				}

				for (Location path : dependencyPath.getPath())
				{
					model.remove(GridWorldModel.IN_USE, path);
				}				

				if (getAgentToBox(goal.getBox(), agent))
				{
					getObjectToLocation(goal.getBox(), goal.getLocation(), agent);
					break;
				}
			}
			
			queue.add(agent);
		}
	}
	

	public boolean getAgentToBox(Box box, Agent agent)
	{
		int initialStep = getInitialStep(agent);

		List<Action> actions = PathfindingSearch.search(agent, agent, box.getLocation(), 1, initialStep);

		if (actions == null)
		{
			logger.info(agent.getName() + " could not find path to box " + box.getLetter());
			return false;			
		}

		System.err.print  (actions );

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
	
	public boolean getObjectToLocation(Cell tracked, Location loc, Agent agent)
	{
		int initialStep = getInitialStep(agent);
		
		List<Action> actions = PathfindingSearch.search(agent, tracked, loc, 0, initialStep);

		if (actions == null)
		{
			logger.info(agent.getName() + " could not find path to location " + loc);
			return false;			
		}
		
		System.err.println(actions);
		
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
		return actions.get(agent.getNumber()).size() + 1;
	}
	
	public boolean hasAgentWithOppositeAction(int step, Action action)
	{
		if (!hasModel(step))
		{
			return false;
		}
		else
		{
			DataWorldModel model = getModel(step);
			
			Agent agent = model.getAgent(action.getNewAgentLocation());
			
			if (agent == null)
			{
				return false;
			}
			else
			{
				return action.isOpposite(actions.get(agent.getNumber()).get(step));
			}
		}
	}
	
	public boolean hasModel(int step)
	{
		return step < gridModels.size();
	}
	
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
	
	private void matchBoxesAndGoals()
	{		
		for (Entry<Character, Set<Goal>> entry : worldModel.getGoalMap().entrySet())
		{
			for (Goal goal : entry.getValue())
			{
				// Create copy of box set
				Set<Box> boxes = new HashSet<>(worldModel.getBoxes(entry.getKey()));
				
				Optional<Box> box = boxes.stream().min((b1, b2) -> d(goal, b1) - d(goal, b2));
				
				if (box.isPresent())
				{
					boxes.remove(box);
					
					goal.setBox(box.get());
				}
				else System.out.println("ERROR: matchBoxesAndGoals()");
			}
		}
	}
	
	private DependencyPath getGoalDependencyPath(Agent agent, Goal goal)
	{
		return DependencyPathSearch.search(goal.getLocation(), goal.getBox().getLocation(), GridWorldModel.BOX | GridWorldModel.AGENT, 0, getInitialStep(agent));		
	}
	
	private DependencyPath getBoxDependencyPath(Agent agent, Box box)
	{		
		return DependencyPathSearch.search(box.getLocation(), agent.getLocation(), GridWorldModel.BOX | GridWorldModel.AGENT, 1, getInitialStep(agent));		
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

	private static int d(Cell c1, Cell c2) {
		return c1.getLocation().distance(c2.getLocation());
	}
}

