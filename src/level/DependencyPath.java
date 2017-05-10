package level;

import java.util.LinkedList;
import java.util.List;

import env.model.GridWorldModel;
import env.planner.Planner;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import level.cell.Goal;
import srch.searches.DependencyPathSearch;

public class DependencyPath {

	private LinkedList<Location> path;
	private LinkedList<Location> dependencies;
	
	public DependencyPath()
	{
		path = new LinkedList<>();
		dependencies = new LinkedList<>();
	}
	
	public void addDependencyPath(DependencyPath dependencyPath)
	{
		path.addAll(dependencyPath.path);
		dependencies.addAll(dependencyPath.dependencies);
	}
	
	public void addToPath(Location l)
	{
		path.addFirst(l);
	}
	
	public void addDependency(Location l)
	{
		addToPath(l);
		dependencies.addFirst(l);
	}
	
	public List<Location> getPath()
	{
		return path;
	}
	
	public List<Location> getDependencies()
	{
		return dependencies;
	}
	
//	public static DependencyPath getGoalDependencyPath(Agent agent, Goal goal)
//	{
//		return getLocationDependencyPath(agent, goal.getBox().getLocation(), goal.getLocation(), 0);
//	}
//	
//	public static DependencyPath getBoxDependencyPath(Agent agent, Box box)
//	{
//		return getLocationDependencyPath(agent, agent.getLocation(), box.getLocation(), 1);
//	}
	
	/**
	 * Get dependency path between the locations. Has a default proximity equal to 0
	 * Important to note that the initial location is added to the path and not
	 * to dependencies.
	 * @param agent
	 * @param from
	 * @param to
	 * @return Dependency between the two locations with a proximity of 1
	 */
//	public static DependencyPath getLocationDependencyPath(Agent agent, Location from, Location to)
//	{
//		return getLocationDependencyPath(agent, from, to, 0);
//	}
	
	/**
	 * Get the dependency path between the locations with a box.
	 * @param agent
	 * @param box
	 * @param to
	 * @return
	 */
	public static DependencyPath getDependencyPath(Agent agent, Box box, GridWorldModel model)
	{
		return getLocationDependencyPath(agent, agent.getLocation(), box.getLocation(), 1, model);
	}
	
	/**
	 * Get the dependency path between the locations.
	 * @param agent
	 * @param to
	 * @return
	 */
	public static DependencyPath getDependencyPath(Agent agent, Cell cell, Location to, GridWorldModel model)
	{
		return getLocationDependencyPath(agent, cell.getLocation(), to, 0, model);
	}
	
	private static DependencyPath getLocationDependencyPath(Agent agent, Location from, Location to, int proximity, GridWorldModel model)
	{		
		int agentNumber = Planner.getInstance().getLastModel().getAgentNumber(agent);
		return DependencyPathSearch.search(from, to, GridWorldModel.BOX | GridWorldModel.AGENT, proximity, agentNumber, model);		
	}
}
