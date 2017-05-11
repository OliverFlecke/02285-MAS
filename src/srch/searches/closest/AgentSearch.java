package srch.searches.closest;

import env.model.DataModel;
import env.model.WorldModel;
import level.Color;
import level.Location;
import srch.Node;
import srch.nodes.ClosestNode;

public class AgentSearch extends ClosestSearch {

	public static Location search(Color color, Location from, DataModel model) 
	{
		return new AgentSearch(color).search(new ClosestNode(from, model));
	}
	
	private Color color;

	public AgentSearch(Color color) 
	{
		super(WorldModel.AGENT);
		
		this.color = color;
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		if (!super.isGoalState(n))
		{
			return false;
		}			
		return color.equals(((ClosestNode) n).getModel().getColor(n.getLocation()));
	}

}
