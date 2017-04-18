package env.planner;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Set;
import env.model.WorldModel;
import jason.environment.grid.Location;
import lvl.cell.Box;
import lvl.cell.Goal;
import srch.clo.CloSearch;
import srch.dep.DepSearch;

public class Planner {
	
	private static WorldModel model;
	
	private static Set<Goal> unsolvedGoals = new HashSet<>();
	
	public static void plan()
	{
		model = WorldModel.getInstance();
		
		unsolvedGoals.addAll(model.getGoals());
		
		matchBoxesAndGoals();
		
		createGoalDependencies();
	}
	
	public static synchronized Location selectGoal(int agX, int agY)
	{
		Location from = new Location(agX, agY);
		
		Location closestLoc = CloSearch.search(WorldModel.GOAL, from);
		
		Goal goal = model.getGoal(closestLoc);
		
		while (goal.hasDependencies())
		{
			goal = goal.getDependency();
		}		
		unsolvedGoals.remove(goal);
		
		return goal.getLocation();
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
