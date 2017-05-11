package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.DataModel;
import level.Direction;
import level.Location;
import srch.Node;

public class StorageNode extends ClosestNode {
	
	private int include;

	public StorageNode(Location initial, int include, DataModel model) {
		super(initial, DataModel.AGENT, model);		
		this.include = include;
	}

	public StorageNode(Node parent, Direction direction, Location location) {
		super(parent, direction, location);		
		this.include = ((StorageNode) parent).include;
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.EVERY.length);
		
		for (Direction dir : Direction.EVERY)
		{
			Location loc = this.getLocation().newLocation(dir);
			
			// TODO: Add agent color
			
			if (getModel().hasObject(include, loc))
			{
				expandedNodes.add(new ClosestNode(this, dir, loc));
			}			
			else if (getModel().isFree(this.getObject(), loc))
			{
				expandedNodes.add(new ClosestNode(this, dir, loc));
			}
		}
		return expandedNodes;
	}
	

}
