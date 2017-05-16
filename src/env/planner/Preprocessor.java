package env.planner;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		
		matchAgentsAndGoals(goals);
		
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
	
	private static void matchAgentsAndGoals(List<Goal> goals)
	{
		for (Goal goal : goals)
		{
			Agent agent = goal.getBox().getAgent();
			
			agent.addGoal(goal);
		}
	}

	private static List<Goal> prioritizeGoals() 
	{
		Map<Goal, Set<SimpleEntry<Goal, Boolean>>> dependencies = new HashMap<>();
		
		for (Goal goal : model.getGoals())
		{
			if (model.isSolved(goal)) continue;
			
			if (!dependencies.containsKey(goal))
			{
				dependencies.put(goal, new HashSet<SimpleEntry<Goal, Boolean>>());
			}
			
			Box 	box 	= goal.getBox();
			Agent 	agent 	= box.getAgent();

	        DependencySearch.search(goal.getLocation(), box.getLocation(), DataModel.GOAL, model)
	        	.stream().forEach(loc -> addDependency(dependencies, loc, goal, true));
	        
	        DependencySearch.search(box.getLocation(), agent.getLocation(), DataModel.BOX | DataModel.GOAL, model)
		        .stream().forEach(loc -> addDependency(dependencies, loc, goal, false));
		}
		
		return dependencies.entrySet().stream()
				.sorted(comparator)
				.map(e -> e.getKey())
				.collect(Collectors.toList());
	}
	
	private static void addDependency(Map<Goal, Set<SimpleEntry<Goal, Boolean>>> dependencies, Location l, Goal goal, boolean isGoalToBox)
	{
		SimpleEntry<Goal, Boolean> entry = new SimpleEntry<>(goal, isGoalToBox);
		
    	if (model.hasObject(DataModel.GOAL, l))
    	{
    		MapUtil.addToMap(dependencies, model.getGoal(l), entry);
    	}
    	else
    	{
    		Goal otherGoal = model.getBox(l).getGoal();
    		if (otherGoal != null)
    			MapUtil.addToMap(dependencies, otherGoal, entry);
    	}
	}
	
	private static Comparator<Entry<Goal, Set<SimpleEntry<Goal, Boolean>>>> comparator 
		= new Comparator<Entry<Goal,Set<SimpleEntry<Goal, Boolean>>>>() {

		@Override
		public int compare(	Entry<Goal, Set<SimpleEntry<Goal, Boolean>>> o1, 
							Entry<Goal, Set<SimpleEntry<Goal, Boolean>>> o2) 
		{
			int size1 = Math.toIntExact(o1.getValue().stream().filter(e -> e.getValue()).count());
			int size2 = Math.toIntExact(o2.getValue().stream().filter(e -> e.getValue()).count());
			
			// Sort by goal to box dependency count
			if (size1 != size2)
			{
				return size1 - size2;
			}
			
			size1 = Math.toIntExact(o1.getValue().stream().filter(e -> !e.getValue()).count());
			size2 = Math.toIntExact(o2.getValue().stream().filter(e -> !e.getValue()).count());

			// Sort by agent to box dependency count
			if (size1 != size2)
			{
				return size1 - size2;
			}			
			
			Goal goal1 = o1.getKey();
			Goal goal2 = o2.getKey();
			
			Box box1 = goal1.getBox();
			Box box2 = goal2.getBox();
			
			int dist1 = box1.getLocation().distance(goal1.getLocation());
			int dist2 = box2.getLocation().distance(goal2.getLocation());
			
			// Sort by distance between box and goal
			if (dist1 != dist2)
			{
				return dist1 - dist2;
			}
			
			Agent agent1 = box1.getAgent();
			Agent agent2 = box2.getAgent();
			
			int agDist1 = agent1.getLocation().distance(box1.getLocation());
			int agDist2 = agent2.getLocation().distance(box2.getLocation());
			
			// Sort by distance between agent and box
			return agDist1 - agDist2;
		}
	};
}
