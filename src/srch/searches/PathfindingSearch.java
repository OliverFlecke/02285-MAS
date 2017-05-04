package srch.searches;

import java.util.List;

import env.model.GridWorldModel;
import env.model.SimulationWorldModel;
import level.cell.Cell;
import level.Location;
import level.action.Action;
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
	public static List<Action> search(GridWorldModel model, Cell agent, Cell tracked, Location to, int initialStep) 
	{
		return new PathfindingSearch(to, 0).search(new PathfindingNode(model, agent, tracked, initialStep));
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
		SimulationWorldModel model = ((PathfindingNode) n).getModel();
		
		int trackedDist = model.getTrackedLocation().distance(model.getAgentLocation());
		int goalDist    = model.getTrackedLocation().distance(goalLocation);
		
		if (trackedDist > 1)
		{
			goalDist += trackedDist;
		}

		goalDist += 10 * model.countUnsolvedGoals();
		
		return goalDist; 
	}
}
