package srch.searches;

import java.util.Map;

import level.Location;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Evaluation.AStar;
import srch.Strategy.BestFirst;
import srch.nodes.DistanceNode;

public class DistanceSearch extends Search implements Heuristic {

	public static Map<Location, Integer> search(Location from, Location to) 
	{
		return new DistanceSearch(to, 0).search(new DistanceNode(from));
	}
	
	private Location goalLocation;
	private int 	 goalDistance;
	
	public DistanceSearch(Location to, int proximity)
	{
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.goalLocation = to;
		this.goalDistance = proximity;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return n.getLocation().distance(goalLocation) == goalDistance;
	}
	
	@Override
	public int h(Node n) 
	{		
		return n.getLocation().distance(goalLocation);
	}
}
