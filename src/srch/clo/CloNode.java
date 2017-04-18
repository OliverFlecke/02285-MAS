package srch.clo;

import java.util.ArrayList;
import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class CloNode extends Node {
	
	public CloNode(Location initial)
	{
		super(initial);
	}

	public CloNode(Node parent, String direction, Location location) 
	{
		super(parent, direction, location);
	}

	@Override
	public List<Node> getExpandedNodes() 
	{
		List<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, this.getLocation());
					
			if (WorldModel.getInstance().isFree(loc, WorldModel.WALL))
			{
				expandedNodes.add(new CloNode(this, dir, loc));
			}
		}		
		
		return expandedNodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Location extractPlan() 
	{
		return this.getLocation();
	}

}
