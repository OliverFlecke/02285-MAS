package srch.str;

import java.util.ArrayList;
import java.util.List;
import jason.environment.grid.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BFS;

public class StrSearch extends Search {
	
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
	public static List<Location> search(Location initial) {
		return (List<Location>) new StrSearch().search(new StrNode(initial, new ArrayList<Location>()));
	}
	
	public StrSearch()
	{
		this.setStrategy(new BFS());
	}

	@Override
	public boolean isGoalState(Node n) {
		return false;
	}
}
