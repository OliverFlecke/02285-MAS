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

	public static DependencyPath search(Location from, Location to, int object, int agentNumber, boolean toBox, DataModel model) 
	{
		return new DependencyPathSearch(to).search(new DependencyPathNode(from, object, agentNumber, toBox, model));
	}
	
	private Location goalLocation;
	
	public DependencyPathSearch(Location to)
	{
		this.setStrategy(new BestFirst(new AStar(this)));
		
		goalLocation = to;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return n.getLocation().distance(goalLocation) == 0;
	}

	@Override
	public int h(Node n) 
	{
		int h = n.getLocation().distance(goalLocation) + ((DependencyPathNode) n).getDependencies() * 10;
		
		h += ((DependencyPathNode) n).getModel().isSolved(n.getLocation()) ? 10 : 0;
		
		return h; 
	}
}
