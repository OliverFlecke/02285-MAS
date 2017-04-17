package srch.str;

import java.util.ArrayList;
import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class StrNode extends Node {
	
	private int object;
	private List<Location> storages;

	public StrNode(Location initial, int object, List<Location> storages) {
		super(initial);
		
		this.object   = object | WorldModel.WALL;
		this.storages = storages;
	}
	
	public StrNode(Node parent, String dir, Location loc) {
		super(parent, dir, loc);

		this.object   = ((StrNode) parent).object;
		this.storages = ((StrNode) parent).storages;
	}

	@Override
	public List<Node> getExpandedNodes() 
	{
		List<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, this.getLocation());

			if (WorldModel.getInstance().isFree(loc, object))
			{
				expandedNodes.add(new StrNode(this, dir, loc));
			}
		}
		
		// This node is at a corner cell
		if (expandedNodes.size() == 1)
		{
			Location loc = this.getLocation();
			
			if (WorldModel.getInstance().isFree(loc, WorldModel.BOX))
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
