package srch.searches.closest;

import java.util.function.Predicate;

import env.model.CellModel;
import env.model.DataModel;
import env.model.OverlayModel;
import env.model.WorldModel;
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
	
	public static Location search(Location from, Agent agent, boolean selfHelp, boolean isAgent, OverlayModel overlay, CellModel model) 
	{
		if (isAgent || WorldModel.getInstance().getFreeCellCount() < 10)
		{
			return new StorageSearch(selfHelp, overlay, n -> true).search(new StorageNode(from, agent, model));
		}
		
		if (WorldModel.getInstance().getFreeCellCount() > 50)
		{
			return new StorageSearch(selfHelp, overlay, isXParentFree(overlay, 1)).search(new StorageNode(from, agent, model));
		}
		
		Location storage = null;
		
		storage = new StorageSearch(selfHelp, overlay, hasXFreeAdjacent(1)).search(new StorageNode(from, agent, model));
		
		if (storage == null)
		{
			storage = new StorageSearch(selfHelp, overlay, hasXFreeAdjacent(2)).search(new StorageNode(from, agent, model));
		}

		if (storage == null)
		{
			storage = new StorageSearch(selfHelp, overlay, isXParentFree(overlay, 10)).search(new StorageNode(from, agent, model));
		}

		if (storage == null)
		{
			storage = new StorageSearch(selfHelp, overlay, isXParentFree(overlay, 5)).search(new StorageNode(from, agent, model));
		}

		if (storage == null)
		{
			storage = new StorageSearch(selfHelp, overlay, isXParentFree(overlay, 3)).search(new StorageNode(from, agent, model));
		}

		if (storage == null)
		{
			storage = new StorageSearch(selfHelp, overlay, isXParentFree(overlay, 2)).search(new StorageNode(from, agent, model));
		}

		if (storage == null)
		{
			storage = new StorageSearch(selfHelp, overlay, isXParentFree(overlay, 1)).search(new StorageNode(from, agent, model));
		}

		if (storage == null)
		{
			storage = new StorageSearch(selfHelp, overlay, isXParentFree(overlay, 0)).search(new StorageNode(from, agent, model));
		}
		
		return storage;
	}
	
	private boolean selfHelp, canTurn;
	private OverlayModel overlay;
	private Predicate<StorageNode> goalPredicate;
	
	public StorageSearch(boolean selfHelp, OverlayModel overlay, Predicate<StorageNode> goalPredicate)
	{
		super();
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.selfHelp = selfHelp;
		this.overlay = overlay;
		this.goalPredicate = goalPredicate;
	}

	@Override
	public boolean isGoalState(Node n) 
	{
		DataModel model = ((StorageNode) n).getModel();
		
		Location loc = n.getLocation();
		
		if (selfHelp && !canTurn) return false;
		
		return overlay.isFree(loc) && model.isFree(loc) && goalPredicate.test((StorageNode) n);
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
	
	private static Predicate<StorageNode> hasXFreeAdjacent(int x) {
		return n -> n.getModel().isFreeAdjacent(n.getAgentNumber(), n.getLocation()) == x;
	}
	
	private static Predicate<StorageNode> isXParentFree(OverlayModel overlay, int x) 
	{		
		return n -> 
		{
			int count = x;
			StorageNode parent = n;
			
			while (count > 0 && parent.getParent() != null)
			{
				count--;
				parent = (StorageNode) parent.getParent();
			}
			
			return overlay.isFree(parent.getLocation());
		};
	}
}
