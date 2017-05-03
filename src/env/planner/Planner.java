package env.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import env.model.DataWorldModel;
import env.model.WorldModel;
import level.Location;
import level.action.Action;
import level.action.SkipAction;
import level.cell.Agent;
import level.cell.AgentComparator;
import level.cell.Box;
import level.cell.Goal;
import srch.searches.DependencySearch;
import srch.searches.PathfindingSearch;

public class Planner {
	
//	private static final Logger logger = Logger.getLogger(Planner.class.getName());
	
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
		
		for (List<Goal> goals; !(goals = getUnsolvedGoals()).isEmpty();)
		{
			execute(goals);
		}
	}
	

	public void execute(List<Goal> goals)
	{
		PriorityQueue<Agent> agents = new PriorityQueue<Agent>(new AgentComparator(this));
		for (Agent agent : worldModel.getAgents()) 
			agents.add(agent);
		
		for (Goal goal : goals)
		{
			
			Agent agent = agents.poll();
			
			Box box = goal.getBox();
			

			int initialStep = getInitialStep(agent);

			List<Action> actions = PathfindingSearch.search(agent.getLocation(), goal.getLocation(), initialStep, agent, box, getModel(initialStep));

			if (actions.isEmpty())
				actions.add(new SkipAction(agent.getLocation()));
	
			System.err.println(actions);
			
			this.actions.get(agent.getNumber()).addAll(actions);
			
			for (Action action : actions)
			{
				getModel(initialStep++).doExecute(action);
			}

			agents.add(agent);
		}
	}
	
	public List<Goal> getUnsolvedGoals()
	{
		
		return null;
	}
	
	public int getInitialStep(Agent agent)
	{
		return actions.get(agent.getNumber()).size() + 1;
	}
	
	public DataWorldModel getModel(int step)
	{
		if (step > gridModels.size())
		{
			throw new UnsupportedOperationException("getModel");
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

	private Collection<Goal> prioritizeGoals(Collection<Goal> goals) 
	{
		Map<Goal, Set<Goal>> goalDependencies = new HashMap<>();
		
		for (Goal goal : goals)
		{
			Location from = goal.getLocation();		
			Location to   = goal.getBox().getLocation();

	        List<Location> locations = DependencySearch.search(from, to, DataWorldModel.GOAL);
	        
	        locations.stream().forEach(loc -> addToMap(goalDependencies, worldModel.getGoal(loc), goal));
		}
		
		return goalDependencies.entrySet().stream()
//		        .sorted(Comparator.comparingInt(e -> e.getValue().size()))
				.filter(e -> e.getValue().isEmpty())
		        .map(Map.Entry::getKey)
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

	private static int d(Goal goal, Box box) {
		return goal.getLocation().distance(box.getLocation());
	}
}

