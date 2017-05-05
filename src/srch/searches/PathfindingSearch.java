package srch.searches;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		return new PathfindingSearch(agent.getLocation(), to).search(new PathfindingNode(agent, tracked, initialStep, planner));
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
		
		int goalDist = 0;
//		int goalDist    = model.getTrackedLocation().distance(goalLocation);

//		goalDist += distances.getOrDefault(n.getLocation(), Integer.MAX_VALUE);
//		if (goalDist == Integer.MAX_VALUE) 
//			goalDist = 5 * model.getTrackedLocation().distance(goalLocation);
		
		Location agLoc = n.getLocation();
		
		if (distances.containsKey(agLoc))
		{
			goalDist += distances.get(agLoc);
		}
		else
		{
			Optional<Location> closestOpt = distances.keySet().stream().min((l1, l2) -> l1.distance(agLoc) - l2.distance(agLoc));
			
			if (closestOpt.isPresent())
			{
				Location closest = closestOpt.get();
				
				goalDist += distances.get(closest);
				goalDist += agLoc.distance(closest);
			}
			else
			{
				System.err.println("distance map empty!");
			}
		}

		goalDist += 10 * model.countUnsolvedGoals();
		
//		goalDist += 10 * ((PathfindingNode) n).getSkipCount();
		
//		int trackedDist = model.getTrackedLocation().distance(model.getAgentLocation());
//		if (trackedDist > 1)
//		{
//			goalDist += trackedDist;
//		}

		return goalDist; 
	}
}
