package env.model;

import jason.environment.grid.Location;
import level.Actions.Action;
import level.cell.Agent;
import level.cell.Cell;

public class SimulationWorldModel extends GridWorldModel {
	
	private Cell cell;
	private Agent agent;

	public SimulationWorldModel(GridWorldModel model, Agent agent)
	{
		super(model);
		
		this.agent = new Agent(agent);
	}
	
	public Location getCellLocation()
	{
		return cell.getLocation();
	}
	
	public Location getAgentLocation()
	{
		return agent.getLocation();
	}
	
	public void move(int obj, Location fr, Location to)
	{
		if (cell.getLocation().equals(fr))
		{
			cell.setLocation(to);
		}
		
		if (obj == AGENT)
		{
			agent.setLocation(to);
		}
		
		super.move(obj, fr, to);
	}
    
    public SimulationWorldModel run(Action action)
    {    	
    	SimulationWorldModel simulation = new SimulationWorldModel(this, this.agent);
    	
    	simulation.doExecute(action);
    	
    	return simulation;
    }
}
