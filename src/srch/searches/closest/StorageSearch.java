package srch.searches.closest;

import env.model.GridWorldModel;
import env.planner.Planner;
import level.Location;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.Evaluation.AStar;
import srch.nodes.ClosestNode;

public class StorageSearch extends Search implements Heuristic {
	
	public static Location search(Location from, GridWorldModel model) 
	{
		return new StorageSearch(model).search(new ClosestNode(from, GridWorldModel.AGENT));
	}
	
	private GridWorldModel model;
	
	public StorageSearch(GridWorldModel model)
	{
		super();
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.model = model;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return model.isFree(GridWorldModel.IN_USE, n.getLocation());
	}

	@Override
	public int h(Node n) {
		return Planner.getInstance().getUnsolvedGoals().size() * 10;
	}
}
