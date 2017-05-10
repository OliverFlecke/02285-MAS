package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.GridWorldModel;
import level.Direction;
import level.Location;
import srch.Node;

public class StorageNode extends ClosestNode {
	
	private int include;
	private GridWorldModel model;

	public StorageNode(Location initial, int include, GridWorldModel model) {
		super(initial, GridWorldModel.AGENT);
		
		this.include = include;
		this.model	 = model;
	}

	public StorageNode(Node parent, Direction direction, Location location) {
		super(parent, direction, location);
		
		this.include = ((StorageNode) parent).include;
		this.model   = ((StorageNode) parent).model;
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.EVERY.length);
		
		for (Direction dir : Direction.EVERY)
		{
			Location loc = this.getLocation().newLocation(dir);
			
			if (model.hasObject(include, loc))
			{
				expandedNodes.add(new ClosestNode(this, dir, loc));
			}			
			else if (model.isFree(this.getObject(), loc))
			{
				expandedNodes.add(new ClosestNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}
	

}
