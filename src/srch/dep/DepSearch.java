package srch.dep;

import java.util.List;
import jason.environment.grid.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.Evaluation.AStar;
import srch.Heuristic;

public class DepSearch extends Search implements Heuristic {
	
	/**
	 * Returns an ordered list of strings corresponding to the sequence of
	 * directions to move to get to the goal, depending on the proximity.
	 * The A* considers all boxes as obstacles, even if the box is on the 
	 * goal location. 
	 * @param from - Initial location.
	 * @param to - Goal location.
	 * @param proximity - The distance between the goal and the solution. 
	 * With proximity = 0, the solution is a path to the goal location.
	 * With proximity = 1, the solution is a path to a cell adjacent to the goal location.
	 * @return Ordered list of directions leading to the goal.
	 */
	@SuppressWarnings("unchecked")
	public static List<Location> search(Location from, Location to) {
		return (List<Location>) new DepSearch(to).search(new DepNode(from));
	}
	
	private Location goalLocation;
	
	public DepSearch(Location to)
	{
		this.setStrategy(new BestFirst(new AStar(this)));
		
		goalLocation = to;
	}

	@Override
	public boolean isGoalState(Node n) {
		return n.getLocation().distance(goalLocation) == 0;
	}

	@Override
	public int h(Node n) {
		return n.getLocation().distance(goalLocation) + ((DepNode) n).getDependencies() * 10; 
	}
}
