package srch.searches.closest;

import env.model.CellModel;
import env.model.DataModel;
import env.model.OverlayModel;
import level.Direction;
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
	
	public static Location search(Location from, Agent agent, OverlayModel overlay, CellModel model) 
	{
		return new StorageSearch(agent.getLocation(), from, overlay).search(new StorageNode(from, agent, model));
	}
	
	private Direction dirBoxToAgent;
	private OverlayModel overlay;
	
	public StorageSearch(Location agent, Location tracked, OverlayModel overlay)
	{
		super();
		
		this.setStrategy(new BestFirst(new AStar(this)));
		
		this.dirBoxToAgent = tracked.inDirection(agent);
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
		DataModel 	model 	= Getter.getModel(n);		
		Location 	loc 	= n.getLocation();
		int			h		= 0;
		
		h += Getter.getDirection(n).equals(dirBoxToAgent) ? 3 : 0;
		
		// Do not add agent or box penalty if location contains the agent itself
		if (model.hasObject(((StorageNode) n).getAgentNumber(), DataModel.BOX_MASK, loc))
		{
			return h;
		}
		
		h += model.hasObject(DataModel.AGENT, loc) || model.hasObject(DataModel.BOX, loc) ? 100 : 0;
		
		return h;
	}
}
