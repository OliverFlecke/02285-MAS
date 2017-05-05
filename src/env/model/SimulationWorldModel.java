package env.model;

import env.planner.Planner;
import level.Direction;
import level.Location;
import level.action.Action;
import level.action.MoveAction;
import level.action.PullAction;
import level.action.PushAction;
import level.cell.Agent;
import level.cell.Cell;

public class SimulationWorldModel extends GridWorldModel {
	
	private int step;
	private Agent agent;
	private Cell tracked;
	private static Planner planner;
	private boolean hasFailed = false;

	public SimulationWorldModel(GridWorldModel model, int step, Agent agent, Cell tracked)
	{
		super(model);
		
		this.step		= step;
		this.agent 		= new Agent(agent);
		this.tracked  	= new Cell(tracked);
	}
	
	public static void setPlanner(Planner planner)
	{
		SimulationWorldModel.planner = planner;
	}
	
	public Location getTrackedLocation()
	{
		return tracked.getLocation();
	}
	
	public Location getAgentLocation()
	{
		return agent.getLocation();
	}
	
	public boolean hasFailed() 
	{
		return hasFailed;
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
    	SimulationWorldModel simulation = new SimulationWorldModel(this, step + 1, this.agent, this.tracked);
    	
    	simulation.doExecute(action);
    	
    	return simulation;
    }

	public boolean canExecute(Action action) 
	{		
        switch(action.getType())
        {
        case MOVE: return canMove((MoveAction) action);
        case PUSH: return canPush((PushAction) action);
        case PULL: return canPull((PullAction) action);
        case SKIP: return true;
        }
        throw new UnsupportedOperationException("Invalid action: " + action.getType());        
	}

    public synchronized boolean canMove(MoveAction action) 
    {
    	Direction 	dir 	= action.getDirection();
    	
    	Location 	agLoc 	= action.getAgentLocation();
        
        if (agLoc == null) return false;        
        
        Location nAgLoc = Location.newLocation(dir, agLoc);
        
        if (nAgLoc == null) return false;
        
        if (!isFree(nAgLoc)) return false;
        
//        if (planner.hasModel(step) && !planner.getModel(step).isFree(nAgLoc)) 
//    	{
//        	hasFailed = true;
//        	return false;
//    	}
        
        if (planner.hasModel(step))
        {
            int agNumber = this.agent.getNumber();
            
            DataWorldModel model = planner.getModel(step);
            
            int otherAgNumber = Character.getNumericValue((char) (model.getMasked(BOX_MASK, agLoc) >> 24));            
            
            if (model.hasObject(AGENT, agLoc) && agNumber != otherAgNumber)
        	{
            	hasFailed = true;
            	return false;
        	}
            
            otherAgNumber = Character.getNumericValue((char) (model.getMasked(BOX_MASK, nAgLoc) >> 24));
            if (model.hasObject(BOX | LOCKED, nAgLoc) || (model.hasObject(AGENT, nAgLoc) && agNumber != otherAgNumber))
            {
            	hasFailed = true;
            	return false;
            }
        }

        return true;
    }	
    
    public boolean canPush(PushAction action)
    {
    	Direction 	dir1 	= action.getAgentDir();
    	Direction 	dir2 	= action.getBoxDir();
    	
    	Location 	agLoc 	= action.getAgentLocation();
        
        if (agLoc == null) return false;
        
        int agColor = getMasked(COLOR_MASK, agLoc);
        
    	Location boxLoc = Location.newLocation(dir1, agLoc);
        
        if (boxLoc == null) return false;
    	
    	if (isFree(BOX, boxLoc)) return false;
        
        int boxColor = getMasked(COLOR_MASK, boxLoc);
        
        if (agColor != boxColor) return false;
    	
    	Location nBoxLoc = Location.newLocation(dir2, boxLoc);
        
        if (nBoxLoc == null) return false;
        
        if (!isFree(nBoxLoc)) return false;
        
        if (planner.hasModel(step) && !planner.getModel(step).isFree(nBoxLoc))
    	{
        	hasFailed = true;
        	return false;
    	}
    	
		return true;
    }
    
    public boolean canPull(PullAction action)
    {
    	Direction 	dir1 	= action.getAgentDir();
    	Direction 	dir2 	= action.getBoxDir();
    	
    	Location 	agLoc 	= action.getAgentLocation();
        
        if (agLoc == null) return false;
        
        int agColor = getMasked(COLOR_MASK, agLoc);
        
    	Location boxLoc = Location.newLocation(dir2, agLoc);
    	
    	if (boxLoc == null) return false;
    	
    	if (isFree(BOX, boxLoc)) return false;
    	
    	int boxColor = getMasked(COLOR_MASK, boxLoc);
    	
    	if (agColor != boxColor) return false;
    	
    	Location nAgLoc = Location.newLocation(dir1, agLoc);
    	
    	if (nAgLoc == null) return false;
    	
    	if (!isFree(nAgLoc)) return false; 
        
        if (planner.hasModel(step) && !planner.getModel(step).isFree(nAgLoc))
    	{
        	hasFailed = true;
        	return false;
    	}

        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
    	return !planner.hasModel(step) && super.equals(obj);
    }
}
