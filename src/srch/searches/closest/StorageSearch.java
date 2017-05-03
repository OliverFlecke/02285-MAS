package srch.searches.closest;

import env.model.DataWorldModel;
import env.model.GridWorldModel;
import level.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BFS;
import srch.nodes.StorageNode;

public class StorageSearch extends Search {
	
	public static Location search(Location initial, GridWorldModel localModel) 
	{
		return new StorageSearch(localModel).search(new StorageNode(initial));
	}
	
	private GridWorldModel localModel;
	
	public StorageSearch(GridWorldModel localModel)
	{
		this.setStrategy(new BFS());
		
		this.localModel = localModel;
	}

	@Override
	public boolean isGoalState(Node n) {
		return localModel.isFree(DataWorldModel.LOCKED, n.getLocation());
	}
}
