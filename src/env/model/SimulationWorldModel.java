package env.model;

import level.Location;
import level.action.Action;
import level.cell.Cell;

public class SimulationWorldModel extends GridWorldModel {
	
	private Cell agent, tracked;

	public SimulationWorldModel(GridWorldModel model, Cell agent, Cell tracked)
	{
		super(model);
		
		this.agent 		= new Cell(agent);
		this.tracked  	= new Cell(tracked);
	}
	
	public Location getTrackedLocation()
	{
		return tracked.getLocation();
	}
	
	public Location getAgentLocation()
	{
		return agent.getLocation();
	}
	
	public void move(int obj, Location fr, Location to)
	{
		if (tracked.getLocation().equals(fr))
		{
			tracked.setLocation(to);
		}
		
		if (obj == AGENT)
		{
			agent.setLocation(to);
		}
		
		super.move(obj, fr, to);
	}
    
    public SimulationWorldModel run(Action action)
    {    	
    	SimulationWorldModel simulation = new SimulationWorldModel(this, this.agent, this.tracked);
    	
    	simulation.doExecute(action);
    	
    	return simulation;
    }
}
