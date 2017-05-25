package srch.searches;

import level.DependencyPath;
import level.Location;
import level.cell.Agent;
import srch.Evaluation.AStar;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.nodes.DependencyPathNode;

public class DependencyPathSearch extends Search implements Heuristic {

	public static DependencyPath search(Agent agent, Location from, Location to, int object, boolean toBox, int initialStep) 
	{
		return new DependencyPathSearch(to).search(new DependencyPathNode(from, agent, object, toBox, initialStep));
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
		int h = 0;
		
		h += n.getLocation().distance(goalLocation);
		
		h += ((DependencyPathNode) n).getDependencyCount() * 25;
		
		h += ((DependencyPathNode) n).getModel().isSolved(n.getLocation()) ? 10 : 0;
		
		return h; 
	}
}
