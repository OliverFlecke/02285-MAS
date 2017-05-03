package srch.searches;

import java.util.List;

import env.model.GridWorldModel;
import jason.environment.grid.Location;
import level.Actions.Action;
import level.cell.Cell;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.nodes.PathfindingNode;
import srch.Evaluation.AStar;
import srch.Heuristic;

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
	public static List<Action> search(Location from, Location to, int initialStep, Cell agent, Cell tracked, GridWorldModel model) 
	{
		return new PathfindingSearch(to, 0).search(new PathfindingNode(from, initialStep, agent, tracked, model));
	}
	
	private Location goalLocation;
	private int 	 goalDistance;
	
	public PathfindingSearch(Location to, int proximity)
	{
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.goalLocation = to;
		this.goalDistance = proximity;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return ((PathfindingNode) n).getTrackedLoc().distance(goalLocation) == goalDistance;
	}

	@Override
	public int h(Node n) 
	{
		int trackedDist = ((PathfindingNode) n).getTrackedLoc().distance(n.getLocation());
		int goalDist    = ((PathfindingNode) n).getTrackedLoc().distance(goalLocation);
		
		if (trackedDist > 1)
		{
			return trackedDist + goalDist;
		}		
		return goalDist; 
	}
}
