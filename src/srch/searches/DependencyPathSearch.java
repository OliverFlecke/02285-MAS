package srch.searches;

import env.model.DataModel;
import level.DependencyPath;
import level.Location;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Evaluation.AStar;
import srch.Strategy.BestFirst;
import srch.nodes.DependencyPathNode;

public class DependencyPathSearch extends Search implements Heuristic {

	public static DependencyPath search(Location from, Location to, int object, int proximity, int agentNumber, DataModel model) 
	{
		return new DependencyPathSearch(to, proximity).search(new DependencyPathNode(from, object, agentNumber, model));
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
	public boolean isGoalState(Node n) 
	{
		return n.getLocation().distance(goalLocation) == goalDistance;
	}

	@Override
	public int h(Node n) 
	{
		int h = n.getLocation().distance(goalLocation) + ((DependencyPathNode) n).getDependencies() * 10;
		
		h += ((DependencyPathNode) n).getModel().isSolved(n.getLocation()) ? 10 : 0;
		
		return h; 
	}
}
