package level;

import java.util.LinkedList;
import java.util.List;

import env.model.DataModel;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
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
		return getLocationDependencyPath(agent, agent.getLocation(), box.getLocation(), true, model);
	}
	
	/**
	 * Get the dependency path between the locations.
	 * @param agent
	 * @param to
	 * @return
	 */
	public static DependencyPath getDependencyPath(Agent agent, Cell tracked, Location to, DataModel model)
	{
		return getLocationDependencyPath(agent, tracked.getLocation(), to, false, model);
	}
	
	private static DependencyPath getLocationDependencyPath(Agent agent, Location from, Location to, boolean toBox, DataModel model)
	{
		return DependencyPathSearch.search(agent, from, to, DataModel.BOX | DataModel.AGENT, toBox, model);		
	}
}
