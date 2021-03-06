package srch.searches;

import java.util.List;

import env.model.CellModel;
import level.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.nodes.DependencyNode;
import srch.Evaluation.AStar;
import srch.Heuristic;

public class DependencySearch extends Search implements Heuristic {

	/**
	 * 
	 * @param from
	 * @param to
	 * @param object
	 * @return
	 */
	public static List<Location> search(Location from, Location to, int object, CellModel model) 
	{
		return new DependencySearch(to).search(new DependencyNode(from, object, model));
	}
	
	private Location goalLocation;
	
	public DependencySearch(Location to)
	{
		this.setStrategy(new BestFirst(new AStar(this)));
		
		goalLocation = to;
	}

	@Override
	public boolean isGoalState(Node n) {
		return n.getLocation().distance(goalLocation) == 1;
	}

	@Override
	public int h(Node n) 
	{
		int h = 0;
		
		h += n.getLocation().distance(goalLocation);
		
		h += ((DependencyNode) n).getDependencyCount() * 10;
		
		return h; 
	}
}
