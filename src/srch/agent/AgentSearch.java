package srch.agent;

import env.model.WorldModel;
import jason.environment.grid.Location;
import lvl.cell.Agent;
import srch.Node;
import srch.clo.CloNode;
import srch.clo.CloSearch;

public class AgentSearch extends CloSearch {

	public static Location search(String color, Location from) 
	{
		return new AgentSearch(color).search(new CloNode(from));
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
