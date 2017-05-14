package env.planner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import env.model.CellModel;
import env.model.DataModel;
import level.Location;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Goal;
import logging.LoggerFactory;
import srch.searches.DependencySearch;
import srch.searches.closest.AgentSearch;
import srch.searches.closest.BoxSearch;
import util.MapUtil;

public class Preprocessor {
	
	private static final Logger logger = LoggerFactory.getLogger(Preprocessor.class.getName());
	
	private static CellModel model;

	private Preprocessor() {}
	
	public static List<Goal> preprocess(CellModel model)
	{		
		Preprocessor.model = model;
		
		long startTime = System.nanoTime();
		
		matchBoxesAndGoals();
		
		matchAgentsAndBoxes();
		
		List<Goal> goals = prioritizeGoals();
		
		logger.info("Preprocessing done: " + ((System.nanoTime() - startTime) / 1000000000.0));
		
		return goals;
	}
	
	private static void matchBoxesAndGoals()
	{		
		Set<Box> availableBoxes = model.getBoxes().stream()
				.filter(box -> !model.isSolved(box))
				.collect(Collectors.toSet());
		
		Set<Goal> unsolvedGoals = model.getGoals().stream()
				.filter(goal -> !model.isSolved(goal))
				.collect(Collectors.toSet());
		
		for (Goal goal : unsolvedGoals)
		{			
			Location boxLoc = BoxSearch.search(availableBoxes, goal.getLetter(), goal.getLocation(), model);
			
			Box box = model.getBox(boxLoc);

			if (box != null && availableBoxes.remove(box))
			{
				goal.setBox(box);
				box.setGoal(goal);
			}
			else logger.warning("ERROR: matchBoxesAndGoals()");
		}
	}
	
	private static void matchAgentsAndBoxes()
	{
		Set<Box> boxes = model.getGoals().stream()
							.map(goal -> goal.getBox())
							.collect(Collectors.toSet());
		
		if (model.getAgents().length == 1)
		{
			Agent agent = model.getAgent(0);
			
			boxes.stream().forEach(box -> box.setAgent(agent));
		}
		else
		{
			for (Box box : boxes)
			{			
				Location agLoc = AgentSearch.search(box.getColor(), box.getLocation(), model);
				
				Agent agent = model.getAgent(agLoc);
				
				if (agent != null)
				{
					box.setAgent(agent);
				} 
				else logger.warning("ERROR: matchAgentsAndBoxes()");
			}
		}		
	}

	private static List<Goal> prioritizeGoals() 
	{
		Map<Goal, Set<Goal>> dependencies = new HashMap<>();
		
		for (Goal goal : model.getGoals())
		{
			if (model.isSolved(goal)) continue;
			
			if (!dependencies.containsKey(goal))
			{
				dependencies.put(goal, new HashSet<Goal>());
			}
			
			Box 	box 	= goal.getBox();
			Agent 	agent 	= box.getAgent();

	        DependencySearch.search(goal.getLocation(), box.getLocation(), DataModel.BOX | DataModel.GOAL, model)
	        	.stream().forEach(loc -> addDependency(dependencies, loc, goal));
	        
	        DependencySearch.search(box.getLocation(), agent.getLocation(), DataModel.BOX | DataModel.GOAL, model)
		        .stream().forEach(loc -> addDependency(dependencies, loc, goal));
		}
		
		return dependencies.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getValue().size()))
				.map(e -> e.getKey())
				.collect(Collectors.toList());
	}
	
	private static void addDependency(Map<Goal, Set<Goal>> dependencies, Location l, Goal goal)
	{
    	if (model.hasObject(DataModel.GOAL, l))
    	{
    		MapUtil.addToMap(dependencies, model.getGoal(l), goal);
    	}
    	else
    	{
    		Goal otherGoal = model.getBox(l).getGoal();
    		if (otherGoal != null)
    			MapUtil.addToMap(dependencies, otherGoal, goal);
    	}
	}
}
