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
import srch.interfaces.Getter;
import srch.nodes.StorageNode;

public class StorageSearch extends Search implements Heuristic {
	
	public static Location search(Location from, Agent agent, boolean selfHelp, OverlayModel overlay, CellModel model) 
	{
		return new StorageSearch(selfHelp, overlay).search(new StorageNode(from, agent, model));
	}
	
	private boolean selfHelp, canTurn;
	private OverlayModel overlay;
	
	public StorageSearch(boolean selfHelp, OverlayModel overlay)
	{
		super();
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.selfHelp = selfHelp;
		this.overlay = overlay;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		DataModel model = ((StorageNode) n).getModel();
		
		Location loc = n.getLocation();
		
		if (selfHelp && !canTurn) return false;
		
		Node parent = n.getParent();
		
		if (parent != null)
		{
			Location parentLoc = parent.getLocation();
			
			return overlay.isFree(parentLoc) && model.isFree(parentLoc) &&
				   overlay.isFree(loc) && model.isFree(loc);
		}
		
		return overlay.isFree(loc) && model.isFree(loc);
	}

	@Override
	public int h(Node n) 
	{
		DataModel 	model 	= Getter.getModel(n);		
		Location 	loc 	= n.getLocation();
		int			h		= 0;
		
		if (!canTurn && Getter.getModel(n.getParent()).isFreeAdjacent(((StorageNode) n).getAgentNumber(), n.getParent().getLocation()) >= 3) 
		{
			canTurn = true;
		}
		
		// Do not add agent or box penalty if location contains the agent itself
		if (model.hasObject(((StorageNode) n).getAgentNumber(), DataModel.BOX_MASK, loc))
		{
			return h;
		}
		
		h += model.hasObject(DataModel.AGENT, loc) || model.hasObject(DataModel.BOX, loc) ? 100 : 0;
		
		return h;
	}
}
