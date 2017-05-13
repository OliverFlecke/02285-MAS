package env.planner;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import env.model.CellModel;
import env.model.WorldModel;
import level.Location;
import level.cell.*;
import logging.LoggerFactory;
import srch.searches.DependencySearch;
import srch.searches.closest.AgentSearch;
import srch.searches.closest.BoxSearch;
import util.MapUtil;

public class Preprocessor {
	
	private static final Logger logger = LoggerFactory.getLogger(Preprocessor.class.getName());
	
	private static WorldModel worldModel;

	private Preprocessor() {}
	
	public static void preprocess()
	{
		long startTime = System.nanoTime();
		
		worldModel = WorldModel.getInstance();
		
		matchBoxesAndGoals();
		
		matchAgentsAndBoxes();
		
		logger.info("Preprocessing done: " + ((System.nanoTime() - startTime) / 1000000000.0));
	}
	
	private static void matchBoxesAndGoals()
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
	
	private static void matchAgentsAndBoxes()
	{
		Set<Box> boxes = worldModel.getGoals().stream()
							.map(goal -> goal.getBox())
							.collect(Collectors.toSet());
		
		for (Box box : boxes)
		{
			Location agLoc = AgentSearch.search(box.getColor(), box.getLocation(), worldModel);
			
			Agent agent = worldModel.getAgent(agLoc);
			
			if (agent != null)
			{
				box.setAgent(agent);
			} 
			else logger.warning("ERROR: matchAgentsAndBoxes()");
		}
	}
	
	public static List<Goal> prioritizeGoals(CellModel model)
	{
		Set<Goal> goals = model.getGoals();
		
		Map<Goal, Set<Goal>> dependencies = new HashMap<>();
		
		for (Goal goal : goals)
		{			
			if (model.isSolved(goal.getLocation())) continue;
			
			if (!dependencies.containsKey(goal))
			{
				dependencies.put(goal, new HashSet<Goal>());
			}
			
			Box 	box 	= goal.getBox();
			Agent 	agent 	= box.getAgent();

	        DependencySearch.search(goal.getLocation(), box.getLocation(), WorldModel.BOX | WorldModel.GOAL)
	        	.stream().forEach(loc -> addDependency(model, dependencies, loc, goal));
	        
	        DependencySearch.search(box.getLocation(), agent.getLocation(), WorldModel.BOX | WorldModel.GOAL)
		        .stream().forEach(loc -> addDependency(model, dependencies, loc, goal));	
	       
		}
		return dependencies.entrySet().stream()
		        .sorted(Comparator.comparingInt(e -> e.getValue().size()))
		        .map(Map.Entry::getKey)
		        .collect(Collectors.toList());
	}
	
	private static void addDependency(CellModel model, Map<Goal, Set<Goal>> dependencies, Location l, Goal goal)
	{
    	if (model.hasObject(WorldModel.GOAL, l))
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

//	private Collection<Goal> getSolvableGoals(Collection<Goal> goals, Agent agent) 
//	{
//		Map<Goal, Set<Goal>> goalDependencies = new HashMap<>();
//		
//		for (Goal goal : goals)
//		{
//			if (!goalDependencies.containsKey(goal))
//			{
//				goalDependencies.put(goal, new HashSet<Goal>());
//			}
//			
//			Location from = goal.getLocation();
//			Location to   = goal.getBox().getLocation();
//
//	        List<Location> locations = DependencySearch.search(from, to, DataWorldModel.GOAL);
//	        
//	        locations.stream().forEach(loc -> MapUtil.addToMap(goalDependencies, worldModel.getGoal(loc), goal));
//		}
//		
//		return goalDependencies.entrySet().stream()
////		        .sorted(Comparator.comparingInt(e -> e.getValue().size()))
//				.filter(e -> e.getValue().isEmpty() && e.getKey().getBox().getColor().equals(agent.getColor()))
//		        .map(Map.Entry::getKey)
////		        .sorted((g1, g2) -> d(g1, agent) - d(g2, agent))
//		        .collect(Collectors.toList());
//	}
}
