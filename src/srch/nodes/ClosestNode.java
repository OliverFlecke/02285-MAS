package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.WorldModel;
import jason.environment.grid.Location;
import srch.Node;

public class ClosestNode extends Node {
	
	public ClosestNode(Location initial) {
		super(initial);
	}
	
	public ClosestNode(Location initial, int object) {
		super(initial, object);
	}

	public ClosestNode(Node parent, String direction, Location location) {
		super(parent, direction, location);
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(WorldModel.DIRECTIONS.length);
		
		for (String dir : WorldModel.DIRECTIONS)
		{
			Location loc = WorldModel.newLocation(dir, this.getLocation());
			
			if (WorldModel.getInstance().isFree(this.getObject(), loc))
			{
				expandedNodes.add(new ClosestNode(this, dir, loc));
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
