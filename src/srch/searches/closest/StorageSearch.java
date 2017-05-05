package srch.searches.closest;

import env.model.GridWorldModel;
import level.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BFS;
import srch.nodes.ClosestNode;

public class StorageSearch extends Search {
	
	public static Location search(Location from, GridWorldModel model) 
	{
		return new StorageSearch(model).search(new ClosestNode(from, GridWorldModel.AGENT));
	}
	
	private GridWorldModel model;
	
	public StorageSearch(GridWorldModel model)
	{
		super();
		
		this.setStrategy(new BFS());
		
		this.model = model;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return model.isFree(GridWorldModel.IN_USE, n.getLocation());
	}
}
