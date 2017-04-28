package srch.searches.closest;

import env.model.WorldModel;
import jason.environment.grid.Location;
import lvl.cell.Agent;
import srch.Node;
import srch.nodes.ClosestNode;

public class AgentSearch extends ClosestSearch {

	public static Location search(String color, Location from) 
	{
		return new AgentSearch(color).search(new ClosestNode(from));
	}
	
	private String color;

	public AgentSearch(String color) 
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
		
		Agent agent = WorldModel.getInstance().getAgent(n.getLocation());		
		
		return agent.getColor().equals(color);
	}

}