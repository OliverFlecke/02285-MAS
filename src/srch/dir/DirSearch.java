package srch.dir;

import java.util.LinkedList;
import java.util.function.Function;

import jason.environment.grid.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.Evaluation.AStar;

public class DirSearch extends Search {
	
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
	public static LinkedList<String> search(Location from, Location to, int proximity) {
		return new DirSearch(to, proximity).search(new DirNode(from));
	}
	
	private Location goalLocation;
	private int 	 goalDistance;
	
	public DirSearch(Location to, int proximity)
	{
		this.setStrategy(new BestFirst(new AStar(heuristic)));
		
		goalLocation = to;
		goalDistance = proximity;
	}

	@Override
	public boolean isGoalState(Node n) {
		return n.getLocation().distance(goalLocation) == goalDistance;
	}
	
	private Function<Node, Integer> heuristic = (Node n) -> 
	{ 
		return n.getLocation().distance(goalLocation); 
	};
}
