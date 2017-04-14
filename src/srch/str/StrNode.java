package srch.str;

import java.util.ArrayList;
import java.util.List;

import env.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class StrNode extends Node {
	
	private List<Location> storages;

	public StrNode(Location initial, List<Location> storages) {
		super(initial);
		
		this.storages = storages;
	}
	
	public StrNode(Node parent, String dir, Location loc, List<Location> storages) {
		super(parent, dir, loc);

		this.storages = storages;
	}

	@Override
	public List<Node> getExpandedNodes() 
	{
		List<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, this.getLocation());

			if (WorldModel.getInstance().isFreeOfObstacle(loc))
			{
				expandedNodes.add(new StrNode(this, dir, loc, storages));
			}
		}
		
		// This node is at a corner cell
		if (expandedNodes.size() == 1)
		{
			Location loc = this.getLocation();
			
			if (WorldModel.getInstance().isFree(WorldModel.BOX, loc))
			{
				storages.add(loc);
			}
		}
		
		return expandedNodes;
	}

	@Override
	public List<Location> extractPlan() 
	{		
		return storages;
	}

}
