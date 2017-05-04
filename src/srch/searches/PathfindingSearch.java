package srch.searches;

import java.util.List;
import java.util.Map;

import env.model.SimulationWorldModel;
import env.planner.Planner;
import level.cell.Agent;
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
	public static List<Action> search(Agent agent, Cell tracked, Location to, int initialStep, Planner planner) 
	{
		return new PathfindingSearch(to, agent.getLocation()).search(new PathfindingNode(agent, tracked, initialStep, planner));
	}
	
	private Map<Location, Integer> distances;
	private Location goalLocation;
	private int 	 goalDistance;
	
	public PathfindingSearch(Location from, Location to)
	{
		distances = DistanceSearch.search(to, from);
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.goalLocation = to;
		this.goalDistance = 0;
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
		
//		int trackedDist = model.getTrackedLocation().distance(model.getAgentLocation());
//		int goalDist    = model.getTrackedLocation().distance(goalLocation);
		
//		if (trackedDist > 1)
//		{
//			goalDist += trackedDist;
//		}
		
		int goalDist = distances.get(n.getLocation());

		goalDist += 10 * model.countUnsolvedGoals();
		
//		goalDist += 10 * ((PathfindingNode) n).getSkipCount();
		
		return goalDist; 
	}
}
