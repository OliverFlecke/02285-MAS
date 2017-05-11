package level;

import java.util.LinkedList;
import java.util.List;

import env.model.DataModel;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import srch.searches.DependencyPathSearch;
import util.ModelUtil;

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
	
	/**
	 * Get the dependency path between the locations with a box.
	 * @param agent
	 * @param box
	 * @param to
	 * @return
	 */
	public static DependencyPath getDependencyPath(Agent agent, Box box, DataModel model)
	{
		return getLocationDependencyPath(agent, agent.getLocation(), box.getLocation(), 0, model);
	}
	
	/**
	 * Get the dependency path between the locations.
	 * @param agent
	 * @param to
	 * @return
	 */
	public static DependencyPath getDependencyPath(Agent agent, Cell tracked, Location to, DataModel model)
	{
		return getLocationDependencyPath(agent, tracked.getLocation(), to, 0, model);
	}
	
	private static DependencyPath getLocationDependencyPath(Agent agent, Location from, Location to, int proximity, DataModel model)
	{
		return DependencyPathSearch.search(from, to, DataModel.BOX | DataModel.AGENT, proximity, ModelUtil.getAgentNumber(agent), model);		
	}
}
