package srch.searches.closest;

import env.model.GridWorldModel;
import env.planner.Planner;
import level.Location;
import level.cell.Agent;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.Evaluation.AStar;
import srch.nodes.StorageNode;

public class StorageSearch extends Search implements Heuristic {
	
	public static Location search(Location from, Agent agent, GridWorldModel overlay, GridWorldModel model) 
	{
		return new StorageSearch(overlay).search(new StorageNode(from, model.getAgentNumber(agent), model));
	}
	
	private GridWorldModel overlay;
	
	public StorageSearch(GridWorldModel overlay)
	{
		super();
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.overlay = overlay;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		return overlay.isFree(GridWorldModel.IN_USE, n.getLocation());
	}

	@Override
	public int h(Node n) 
	{
		return Planner.getInstance().getUnsolvedGoals().size() * 10;
	}
}
