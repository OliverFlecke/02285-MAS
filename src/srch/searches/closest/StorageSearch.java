package srch.searches.closest;

import env.model.CellModel;
import env.model.DataModel;
import env.model.OverlayModel;
import level.Location;
import level.cell.Agent;
import srch.Evaluation.AStar;
import srch.Heuristic;
import srch.Node;
import srch.Search;
import srch.Strategy.BestFirst;
import srch.nodes.StorageNode;

public class StorageSearch extends Search implements Heuristic {
	
	public static Location search(Location from, Agent agent, OverlayModel overlay, CellModel model) 
	{
		return new StorageSearch(overlay).search(new StorageNode(from, agent, model));
	}
	
	private OverlayModel overlay;
	
	public StorageSearch(OverlayModel overlay)
	{
		super();
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.overlay = overlay;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		DataModel model = ((StorageNode) n).getModel();
		
		Location loc = n.getLocation();
		
		return overlay.isFree(loc) && model.isFree(loc);
	}

	@Override
	public int h(Node n) 
	{
		DataModel model = ((StorageNode) n).getModel();
		
		Location loc = n.getLocation();
		
		if (model.hasObject(((StorageNode) n).getAgentNumber(), DataModel.BOX_MASK, loc))
		{
			return 0;
		}
		
		return model.hasObject(DataModel.AGENT, loc) || model.hasObject(DataModel.BOX, loc) ? 100 : 0;
	}
}
