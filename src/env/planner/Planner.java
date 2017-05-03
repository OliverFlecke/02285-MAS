package env.planner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Set;

import env.model.DataWorldModel;
import env.model.WorldModel;
import level.Location;
import level.action.Action;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Goal;
import srch.searches.DependencySearch;
import srch.searches.PathfindingSearch;
import srch.searches.closest.AgentSearch;

public class Planner {
	
//	private static final Logger logger = Logger.getLogger(Planner.class.getName());
	
	private static WorldModel 	worldModel;	
	private static Set<Goal> 	unsolvedGoals;

	private static ArrayList<DataWorldModel> gridModels;
	
	public static ArrayList<ArrayList<Action>> actions;
	
	public static void plan()
	{
		worldModel = WorldModel.getInstance();
		
		unsolvedGoals = new HashSet<>(worldModel.getGoals());
		
		gridModels = new ArrayList<DataWorldModel>();
		
		gridModels.add(new DataWorldModel(worldModel));
		
		actions = new ArrayList<ArrayList<Action>>(worldModel.getNbAgs());
		
		for (int i = 0; i < worldModel.getNbAgs(); i++)
		{
			actions.add(new ArrayList<Action>());
		}
		
		matchBoxesAndGoals();
		
		createGoalDependencies();
		
		execute();
	}
	
	public static void execute()
	{
		while (!unsolvedGoals.isEmpty())
		{
			Optional<Goal> goalOpt = unsolvedGoals.stream().filter(g -> !g.hasDependencies()).findFirst();
			
			if (goalOpt.isPresent())
			{
				Goal goal 	= goalOpt.get();
				Box box 	= goal.getBox();
				
//				Location agentLoc = AgentSearch.search(box.getColor(), box.getLocation());
				
//				Agent agent = worldModel.getAgent(agentLoc);
				
				Agent agent = worldModel.getAgent(0);
				
				int initialStep = getInitialStep(agent);
				
				List<Action> actions = PathfindingSearch.search(agent.getLocation(), goal.getLocation(), initialStep, agent, box, getModel(initialStep));
				
				System.err.println(actions);
				Planner.actions.get(agent.getNumber()).addAll(actions);
				
				for (Action action : actions)
				{
					getModel(initialStep).doExecute(action);
					
					initialStep++;
				}	
				return;
			}
		}
	}
	
	public static int getInitialStep(Agent agent)
	{
		return actions.get(agent.getNumber()).size() + 1;
	}
	
	public static DataWorldModel getModel(int step)
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
	
	private static void matchBoxesAndGoals()
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

	private static void createGoalDependencies() 
	{
		for (Goal goal : worldModel.getGoals())
		{
			// Important to search from box to goal
			Location from = goal.getBox().getLocation();	
			Location to   = goal.getLocation();		

	        List<Location> dependencies = DependencySearch.search(from, to, DataWorldModel.GOAL);
	        
	        List<Goal> goals = dependencies.stream().map(loc -> worldModel.getGoal(loc))
	        										.collect(Collectors.toList());
	        // Add the dependency chain
	        for (int i = 0; i < goals.size() - 1; i++) 
	        {
	        	goals.get(i).addDependency(goals.get(i + 1));
	        }
		}
	}

	private static int d(Goal goal, Box box) {
		return goal.getLocation().distance(box.getLocation());
	}
}
