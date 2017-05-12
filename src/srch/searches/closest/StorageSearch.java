package srch.searches.closest;

import env.model.DataModel;
import level.Location;
import level.cell.Agent;
import srch.Evaluation.AStar;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.nodes.StorageNode;

public class StorageSearch extends Search implements Heuristic {
	
	public static Location search(Location from, Agent agent, DataModel overlay, DataModel model) 
	{
		return new StorageSearch(overlay).search(new StorageNode(from, agent, model));
	}
	
	private DataModel overlay;
	
	public StorageSearch(DataModel overlay)
	{
		super();
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.overlay = overlay;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return overlay.isFree(DataModel.IN_USE, n.getLocation());
	}

	@Override
	public int h(Node n) 
	{
		return ((StorageNode) n).getModel().isSolved(n.getLocation()) ? 10 : 0;
//		return Planner.getInstance().countUnsolvedGoals() * 10;
	}
}
