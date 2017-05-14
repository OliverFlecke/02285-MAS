package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.CellModel;
import level.Direction;
import level.Location;
import srch.Node;
import srch.interfaces.IDirectionNode;
import srch.interfaces.IModelNode;

public class ClosestNode extends Node implements IDirectionNode, IModelNode {
	
	private Direction direction;
	private CellModel model;
	
	public ClosestNode(Location initial, CellModel model) {
		this(initial, 0, model);
	}
	
	public ClosestNode(Location initial, int object, CellModel model) {
		super(initial, object);
		this.direction 	= null;
		this.model 		= model;
	}

	public ClosestNode(Node parent, Direction direction, Location location) {
		super(parent, location);
		this.direction 	= direction;
		this.model 		= ((ClosestNode) parent).model;
	}

	@Override
	public Direction getDirection() {
		return this.direction;
	}
	
	public CellModel getModel() {
		return this.model;
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.EVERY.length);
		
		for (Direction dir : Direction.EVERY)
		{
			Location loc = this.getLocation().newLocation(dir);
			
			if (model.isFree(this.getObject(), loc))
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
