package srch.searches.closest;

import srch.Node;
import srch.Search;
import srch.Strategy.BFS;
import srch.nodes.ClosestNode;

public abstract class ClosestSearch extends Search {
	
	private int object;
	
	public ClosestSearch(int object)
	{
		this.setStrategy(new BFS());
		
		this.object = object;
	}

	@Override
	public boolean isGoalState(Node n) {
		return ((ClosestNode) n).getModel().hasObject(object, n.getLocation());
	}
}
