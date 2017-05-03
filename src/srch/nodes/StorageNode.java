package srch.nodes;

import level.Location;
import env.model.DataWorldModel;
import level.Direction;
import srch.Node;

public class StorageNode extends ClosestNode {

	public StorageNode(Location initial) {
		super(initial, DataWorldModel.AGENT | DataWorldModel.BOX);
	}
	
	public StorageNode(Node parent, Direction dir, Location loc) {
		super(parent, dir, loc);
	}
}
