package env.planner;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Set;

import env.WorldEnv;
import env.model.WorldModel;
import jason.environment.grid.Location;
import lvl.cell.Agent;
import lvl.cell.Box;
import lvl.cell.Goal;
import srch.agent.AgentSearch;
import srch.dep.DepSearch;
import srch.goal.GoalSearch;
import srch.str.StrSearch;

public class Planner {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Planner.class.getName());
	private static WorldModel model;
	
	private static Set<Goal> unsolvedGoals = new HashSet<>();
	
	public static void plan()
	{
		model = WorldModel.getInstance();
		
		unsolvedGoals.addAll(model.getGoals());
		
		matchBoxesAndGoals();
		
		createGoalDependencies();
	}
	
	public static synchronized Goal selectGoal(int agX, int agY)
	{
		Agent agent = model.getAgent(agX, agY);
		
		Location closestLoc = GoalSearch.search(agent.getColor(), agent.getLocation());
		
		Goal goal = model.getGoal(closestLoc);
		
		while (goal.hasDependencies())
		{
			goal = goal.getDependency();
		}		
		
		if (agent.getColor().equals(goal.getBox().getColor())) 
		{
			unsolvedGoals.remove(goal);
			return goal;
		}
		
		// Merge plan with other agents
		
		return null;
	}
	
	public static synchronized void solveDependencies(int agX, int agY, int boxX, int boxY)
	{
		Agent agent = model.getAgent(agX, agY);
		
		Box box = model.getBox(boxX, boxY);
        
//		logger.info("Asking for help is: " + agent.getName());
        List<Location> locations = DepSearch.search(agent.getLocation(), box.getLocation(), WorldModel.AGENT | WorldModel.BOX);
        
        List<Location> storages = StrSearch.search(agent.getLocation(), 0);
        
        for (Location loc : locations)
        {
//        	logger.info(loc.x + ", " + loc.y + " is a dependency");
        	if (model.hasObject(loc, WorldModel.AGENT))
        	{
        		WorldEnv.getInstance().addAgentPercept(model.getAgent(loc).getName(), WorldEnv.createMovePerception(storages.get(0)));
        	}
        	else if (model.hasObject(loc, WorldModel.BOX))
        	{
        		Location agentLoc = AgentSearch.search(model.getBox(loc).getColor(), loc);
        		WorldEnv.getInstance().addAgentPercept(model.getAgent(agentLoc).getName(), 
        				WorldEnv.createMoveBoxPerception(loc, storages.get(0)));
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

	        List<Location> dependencies = DepSearch.search(from, to, WorldModel.GOAL);
	        
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
