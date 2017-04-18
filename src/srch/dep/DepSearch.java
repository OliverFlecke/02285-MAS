package srch.dep;

import java.util.List;
import jason.environment.grid.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.Evaluation.AStar;
import srch.Heuristic;

public class DepSearch extends Search implements Heuristic {

	/**
	 * 
	 * @param from
	 * @param to
	 * @param object
	 * @return
	 */
	public static List<Location> search(Location from, Location to, int object) {
		return new DepSearch(to).search(new DepNode(from, object));
	}
	
	private Location goalLocation;
	
	public DepSearch(Location to)
	{
		this.setStrategy(new BestFirst(new AStar(this)));
		
		goalLocation = to;
	}

	@Override
	public boolean isGoalState(Node n) {
		return n.getLocation().distance(goalLocation) == 0;
	}

	@Override
	public int h(Node n) {
		return n.getLocation().distance(goalLocation) + ((DepNode) n).getDependencies() * 10; 
	}
}
