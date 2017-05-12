package srch.nodes;

import java.util.ArrayList;
import java.util.List;

import env.model.DataModel;
import level.Direction;
import level.Location;
import level.cell.Agent;
import srch.Node;
import util.ModelUtil;

public class StorageNode extends ClosestNode {
	
	private Agent 	agent;
	private int		agNumber;

	public StorageNode(Location initial, Agent agent, DataModel model) {
		super(initial, DataModel.AGENT | DataModel.BOX, model);
		this.agent 		= agent;		
		this.agNumber 	= ModelUtil.getAgentNumber(agent);
	}

	public StorageNode(Node parent, Direction direction, Location location) {
		super(parent, direction, location);		
		this.agent 		= ((StorageNode) parent).agent;
		this.agNumber 	= ((StorageNode) parent).agNumber;
	}

	@Override
	public List<Node> getExpandedNodes()
	{
		List<Node> expandedNodes = new ArrayList<Node>(Direction.EVERY.length);
		
		for (Direction dir : Direction.EVERY)
		{
			Location loc = this.getLocation().newLocation(dir);
			
				// Add node if loc has agent itself or
			if (this.getModel().hasObject(agNumber, DataModel.BOX_MASK, loc) ||
				// Add node if loc is free of object or
				this.getModel().isFree(this.getObject(), loc))
			{
				expandedNodes.add(new StorageNode(this, dir, loc));
			}
			
			// Add node if loc has box with agent's color and
			// this box can be moved to another loc
			if (this.getModel().hasObject(DataModel.BOX, loc) && 
				this.getModel().getColor(loc).equals(agent.getColor()))
			{
				StorageNode onBox = new StorageNode(this, dir, loc);
				
				if (onBox.getExpandedNodes().size() > 1)
				{
					expandedNodes.add(onBox);
				}
			}
		}
		return expandedNodes;
	}
	

}
