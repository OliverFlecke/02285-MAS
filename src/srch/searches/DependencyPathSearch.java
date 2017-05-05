package srch.searches;

import java.util.List;

import level.DependencyPath;
import level.Location;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Evaluation.AStar;
import srch.Strategy.BestFirst;
import srch.nodes.DependencyPathNode;

public class DependencyPathSearch extends Search implements Heuristic {

	public static DependencyPath search(Location from, Location to, int object, int proximity, int initialStep) 
	{
		return new DependencyPathSearch(to, proximity).search(new DependencyPathNode(from, object, initialStep));
	}
	
	private Location goalLocation;
	private int 	 goalDistance;
	
	public DependencyPathSearch(Location to, int proximity)
	{
		this.setStrategy(new BestFirst(new AStar(this)));
		
		goalLocation = to;
		goalDistance = proximity;
	}

	@Override
	public boolean isGoalState(Node n) {
		return n.getLocation().distance(goalLocation) == goalDistance;
	}

	@Override
	public int h(Node n) {
		return n.getLocation().distance(goalLocation) + ((DependencyPathNode) n).getDependencies() * 10; 
	}
}
