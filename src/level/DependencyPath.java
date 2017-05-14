package level;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import env.model.DataModel;
import env.planner.Planner;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;
import srch.searches.DependencyPathSearch;

public class DependencyPath {

	private LinkedList<Location> path;
	private Map<Location, Integer> dependencies;
	
	public DependencyPath()
	{
		path 			= new LinkedList<>();
		dependencies 	= new HashMap<>();
	}
	
	public void addToPath(Location l)
	{
		path.addFirst(l);
	}
	
	public void addDependency(Location l, int step)
	{
		dependencies.put(l, step);
	}
	
	public List<Location> getPath()
	{
		return path;
	}
	
	public boolean hasDependencies()
	{
		return !dependencies.isEmpty();
	}
	
	public int countDependencies() 
	{
		return dependencies.size();
	}
	
//	public Map<Location, Integer> getDependencies()
//	{
//		return dependencies;
//	}
	
	public Entry<Location, Integer> getDependency(Location loc)
	{
		Planner planner =  Planner.getInstance();
		
		Optional<Entry<Location, Integer>> box = dependencies.entrySet().stream()
				.filter(e -> planner.getModel(e.getValue()).hasObject(DataModel.BOX, e.getKey()))
				.sorted((e1, e2) -> e1.getKey().distance(loc) - e2.getKey().distance(loc))
				.min((e1, e2) -> e1.getValue() - e2.getValue());
		
		if (box.isPresent())
		{
			return box.get();
		}		
		
		return dependencies.entrySet().stream()
				.sorted((e1, e2) -> e1.getKey().distance(loc) - e2.getKey().distance(loc))
				.min((e1, e2) -> e1.getValue() - e2.getValue()).get();
	}
	
	/**
	 * Get the dependency path between the locations with a box.
	 * @param agent
	 * @param box
	 * @param to
	 * @return
	 */
	public static DependencyPath getDependencyPath(Agent agent, Box box, int initialStep)
	{
		return getLocationDependencyPath(agent, agent.getLocation(), box.getLocation(), true, initialStep);
	}
	
	/**
	 * Get the dependency path between the locations.
	 * @param agent
	 * @param to
	 * @return
	 */
	public static DependencyPath getDependencyPath(Agent agent, Cell tracked, Location to, int initialStep)
	{
		return getLocationDependencyPath(agent, tracked.getLocation(), to, false, initialStep);
	}
	
	private static DependencyPath getLocationDependencyPath(Agent agent, Location from, Location to, boolean toBox, int initialStep)
	{
		return DependencyPathSearch.search(agent, from, to, DataModel.BOX | DataModel.AGENT, toBox, initialStep);		
	}
}
