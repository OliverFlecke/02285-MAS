package srch.searches;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import env.model.SimulationModel;
import level.Location;
import level.action.Action;
import level.cell.Cell;
import srch.Evaluation.*;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.nodes.PathfindingNode;

public class PathfindingSearch extends Search implements Heuristic {
	
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
	public static List<Action> search(Cell agent, Cell tracked, Location to, int proximity, int initialStep) 
	{
		return new PathfindingSearch(tracked.getLocation(), to, proximity).search(new PathfindingNode(agent, tracked, initialStep));
	}
	
	private Map<Location, Integer> distances;
	private Location goalLocation;
	private int 	 goalDistance;
	
	public PathfindingSearch(Location from, Location to, int proximity)
	{
		distances = DistanceSearch.search(from, to);
		
		logger.setLevel(Level.OFF);
		
		this.setStrategy(new BestFirst(new AStar(this)));
//		this.setStrategy(new BestFirst(new Greedy(this)));
		
		this.goalLocation = to;
		this.goalDistance = proximity;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return ((PathfindingNode) n).getTrackedLoc().distance(goalLocation) == goalDistance &&
				!((PathfindingNode) n).getModel().isBlocked(n.getLocation());
	}

	@Override
	public int h(Node n) 
	{
		SimulationModel model = ((PathfindingNode) n).getModel();
		
		int goalDist = 0;
		
		Location loc = model.getTrackedLocation();
		
		if (distances.containsKey(loc))
		{
			goalDist += distances.get(loc);
		}
		else
		{
			// Find closest location in distance map if not present
			Location closest = distances.keySet().stream().min((l1, l2) -> l1.distance(loc) - l2.distance(loc)).get();
			
			goalDist += distances.get(closest);
			goalDist += loc.distance(closest);
//			goalDist += 5;
		}

		if (loc.distance(n.getLocation()) > 1)
		{
			goalDist += loc.distance(n.getLocation());
//			goalDist += 5;
		}
		
		goalDist += 10 * model.countUnsolvedGoals();
//		goalDist += goalLocation.distance(loc);
//		goalDist += n.getLocation().distance(loc);
		

		return goalDist; 
	}
}
