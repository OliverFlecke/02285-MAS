package env.planner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Set;

import env.WorldEnv;
import env.model.GridWorldModel;
import env.model.WorldModel;
import jason.asSyntax.Literal;
import jason.environment.grid.Location;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Goal;
import srch.searches.DependencySearch;
import srch.searches.LocationSearch;
import srch.searches.closest.AgentSearch;
import srch.searches.closest.GoalSearch;
import srch.searches.closest.StorageSearch;

public class Planner {
	
	private static final Logger logger = Logger.getLogger(Planner.class.getName());
	
	private static WorldModel 		model;	
	private static GridWorldModel 	localModel;	
//	private static Set<Goal> 		unsolvedGoals = new HashSet<>();

	private static ArrayList<GridWorldModel> models;
	
	public static void plan()
	{
		model = WorldModel.getInstance();
		models = new ArrayList<GridWorldModel>();
		
		localModel = new GridWorldModel(model.deepCopyData());
		
//		unsolvedGoals.addAll(model.getGoals());
		
		matchBoxesAndGoals();
		
		createGoalDependencies();
	}
	
	public static synchronized Goal selectGoal(int agX, int agY)
	{
		Agent agent = model.getAgent(agX, agY);
		
		Location closestLoc = GoalSearch.search(agent.getColor(), agent.getLocation());
		if (closestLoc == null) return null;
		
		Goal goal = model.getGoal(closestLoc);
		
		while (goal.hasDependencies())
		{
			goal = goal.getDependency();
		}		
		
		if (!agent.getColor().equals(goal.getBox().getColor())) 
		{
			logger.warning("Could not find solvable goal");
			return null;
		}
		
		List<Location> path = LocationSearch.search(goal.getLocation(), goal.getBox().getLocation(), 0);
		
		for (Location l : path)
		{
			localModel.lock(l);
		}
		
		// Merge plan with other agents

//		unsolvedGoals.remove(goal);
		return goal;
	}
	
	public static synchronized void solveDependencies(int agX, int agY, int boxX, int boxY)
	{
		Agent agent = model.getAgent(agX, agY);
		
		Box box = model.getBox(boxX, boxY);
        List<Location> dependencies = DependencySearch.search(box.getLocation(), agent.getLocation(), WorldModel.BOX | WorldModel.AGENT);
        
        if (dependencies.size() == 0) return;
        
		List<Location> path = LocationSearch.search(box.getLocation(), agent.getLocation(), 1);
		
		for (Location l : path)
		{
			localModel.lock(l);
		}
        
		// TODO: Include agents
        
        for (Location l : dependencies)
        {
        	Location storage = StorageSearch.search(l, localModel);
        	
        	logger.info("Found storage at: " + storage.toString());
        	
//        	logger.info(l.x + ", " + l.y + " is a dependency");
        	if (model.hasObject(WorldModel.AGENT, l))
        	{
        		Agent otherAgent = model.getAgent(l);
        		
        		if (otherAgent.getNumber() > agent.getNumber()) // Need a better way to figure out who should move out of the way
        		{
        			Literal helpPercept = WorldEnv.createMovePerception(storage);
        			WorldEnv.getInstance().addAgentPercept(otherAgent.getName(), helpPercept);
        		}
        	}
//        	else 
        	if (model.hasObject(WorldModel.BOX, l))
        	{
        		Literal helpPercept = WorldEnv.createMoveBoxPerception(l, storage);
        		
        		Location agentLoc = AgentSearch.search(model.getBox(l).getColor(), l);
        		
        		String agentName = model.getAgent(agentLoc).getName();
        		
        		WorldEnv.getInstance().addAgentPercept(agentName, helpPercept);
        	}
        }
	}
	
	private static void matchBoxesAndGoals()
	{		
		for (Entry<Character, Set<Goal>> entry : model.getGoalMap().entrySet())
		{
			for (Goal goal : entry.getValue())
			{
				// Create copy of box set
				Set<Box> boxes = new HashSet<>(model.getBoxes(entry.getKey()));
				
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
		for (Goal goal : model.getGoals())
		{
			// Important to search from box to goal
			Location from = goal.getBox().getLocation();	
			Location to   = goal.getLocation();		

	        List<Location> dependencies = DependencySearch.search(from, to, WorldModel.GOAL);
	        
	        List<Goal> goals = dependencies.stream().map(loc -> model.getGoal(loc))
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
