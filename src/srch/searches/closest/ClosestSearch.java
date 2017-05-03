package srch.searches.closest;

import env.model.WorldModel;
import level.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BFS;
import srch.nodes.ClosestNode;

public class ClosestSearch extends Search {

	/**
	 * Uses BFS to find the closest object.
	 * @param object
	 * @param from
	 * @return Location of the object.
	 */
	public static Location search(int object, Location from) 
	{
		return new ClosestSearch(object).search(new ClosestNode(from));
	}
	
	private int object;
	
	public ClosestSearch(int object)
	{
		this.setStrategy(new BFS());
		
		this.object = object;
	}

	@Override
	public boolean isGoalState(Node n) {
		return WorldModel.getInstance().hasObject(object, n.getLocation());
	}
}
