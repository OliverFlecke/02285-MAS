package srch.nodes;

import env.model.WorldModel;
import jason.environment.grid.Location;
import level.Direction;
import srch.Node;

public class StorageNode extends ClosestNode {

	public StorageNode(Location initial) {
		super(initial, WorldModel.AGENT | WorldModel.BOX);
	}
	
	public StorageNode(Node parent, Direction dir, Location loc) {
		super(parent, dir, loc);
	}
}
