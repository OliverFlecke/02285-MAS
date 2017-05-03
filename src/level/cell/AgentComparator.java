package level.cell;

import java.util.Comparator;

import env.planner.Planner;

public class AgentComparator implements Comparator<Agent>
{
	private Planner planner;
	
	public AgentComparator(Planner planner)
	{
		this.planner = planner;
	}
	@Override
	public int compare(Agent agent1, Agent agent2) {
		return planner.getActions().get(agent1.getNumber()).size() - planner.getActions().get(agent2.getNumber()).size();
	}
	
}