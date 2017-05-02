package srch.nodes;

import env.model.WorldModel;
import jason.environment.grid.Location;
import level.Direction;
import srch.Node;

public class StorageNode extends ClosestNode {

	public StorageNode(Location initial, int initialStep) {
		super(initial, WorldModel.AGENT | WorldModel.BOX, initialStep);
	}
	
	public StorageNode(Node parent, Direction dir, Location loc) {
		super(parent, dir, loc);
	}
}
