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
import env.model.WorldModel;
import level.Location;
import level.action.Action;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import level.cell.Goal;
import srch.searches.DependencySearch;
import srch.searches.PathfindingSearch;

public class Planner {
	
	private static final Logger logger = Logger.getLogger(Planner.class.getName());
	
	private static WorldModel 	worldModel;	

	private ArrayList<DataWorldModel> gridModels;
	
	public ArrayList<ArrayList<Action>> actions;
	
	public void plan()
	{
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
			
			for (Goal goal : prioritizeGoals(goals, agent))
			{
				if (solveGoal(goal, agent)) 
					break;
			}
			
			queue.add(agent);
		}
	}
	

	public boolean solveGoal(Goal goal, Agent agent)
	{
		Box box = goal.getBox();

		int initialStep = getInitialStep(agent);

		List<Action> actions = PathfindingSearch.search(getModel(initialStep), agent, box, goal.getLocation(), initialStep, this);

		if (actions == null)
		{
			logger.info("Could not solve goal");
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
		
//		step = initialStep;
//		for (Action action : actions)
//		{
//			for (int futureStep = step; futureStep < step + actions.size(); futureStep++)
//			{
//				getModel(futureStep).lock(action.getAgentLocation());				
//			}
//			
//			step++;	
//		}
		return true;
	}
	
	public Collection<Goal> getUnsolvedGoals()
	{
		return getModel(gridModels.size() - 1).getUnsolvedGoals();
	}
	
	public int getInitialStep(Agent agent)
	{
		return actions.get(agent.getNumber()).size() + 1;
	}
	
	public DataWorldModel getModel(int step)
	{
		if (step > gridModels.size())
		{
			throw new UnsupportedOperationException("getModel - step is too large: " + step + " Size is only: " + gridModels.size());
		}
		
		if (step == gridModels.size())
		{
			gridModels.add(new DataWorldModel(gridModels.get(step - 1)));
		}
		
		return gridModels.get(step);
	}
	
	public ArrayList<ArrayList<Action>> getActions()
	{
		return actions;
	}
	
//	public static synchronized Goal selectGoal(int agX, int agY)
//	{
//		Agent agent = worldModel.getAgent(agX, agY);
//		
//		Location closestLoc = GoalSearch.search(agent.getColor(), agent.getLocation());
//		if (closestLoc == null) return null;
//		
//		Goal goal = worldModel.getGoal(closestLoc);
//		
//		while (goal.hasDependencies())
//		{
//			goal = goal.getDependency();
//		}		
//		
//		if (!agent.getColor().equals(goal.getBox().getColor())) 
//		{
//			logger.warning("Could not find solvable goal");
//			return null;
//		}
//		
//		List<Location> path = LocationSearch.search(goal.getLocation(), goal.getBox().getLocation(), 0);
//		
//		for (Location l : path)
//		{
////			localModel.lock(l);
//		}
//		
//		// Merge plan with other agents
//
////		unsolvedGoals.remove(goal);
//		return goal;
//	}
	
//	public static synchronized void solveDependencies(int agX, int agY, int boxX, int boxY)
//	{
//		Agent agent = worldModel.getAgent(agX, agY);
//		
//		Box box = worldModel.getBox(boxX, boxY);
//        List<Location> dependencies = DependencySearch.search(box.getLocation(), agent.getLocation(), WorldModel.BOX | WorldModel.AGENT);
//        
//        if (dependencies.size() == 0) return;
//        
//		List<Location> path = LocationSearch.search(box.getLocation(), agent.getLocation(), 1);
//		
//		for (Location l : path)
//		{
////			localModel.lock(l);
//		}
//        
//		// TODO: Include agents
//        
//        for (Location l : dependencies)
//        {
////        	Location storage = StorageSearch.search(l, localModel);
////        	
////        	logger.info("Found storage at: " + storage.toString());
//        	
////        	logger.info(l.x + ", " + l.y + " is a dependency");
//        	if (worldModel.hasObject(WorldModel.AGENT, l))
//        	{
//        		Agent otherAgent = worldModel.getAgent(l);
//        		
//        		if (otherAgent.getNumber() > agent.getNumber()) // Need a better way to figure out who should move out of the way
//        		{
////        			Literal helpPercept = WorldEnv.createMovePerception(storage);
////        			WorldEnv.getInstance().addAgentPercept(otherAgent.getName(), helpPercept);
//        		}
//        	}
////        	else 
//        	if (worldModel.hasObject(WorldModel.BOX, l))
//        	{
////        		Literal helpPercept = WorldEnv.createMoveBoxPerception(l, storage);
////        		
////        		Location agentLoc = AgentSearch.search(model.getBox(l).getColor(), l);
////        		
////        		String agentName = model.getAgent(agentLoc).getName();
////        		
////        		WorldEnv.getInstance().addAgentPercept(agentName, helpPercept);
//        	}
//        }
//	}
	
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

	private Collection<Goal> prioritizeGoals(Collection<Goal> goals, Agent agent) 
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
				.filter(e -> e.getValue().isEmpty())
		        .map(Map.Entry::getKey)
		        .sorted((g1, g2) -> d(g1, agent) - d(g2, agent))
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

