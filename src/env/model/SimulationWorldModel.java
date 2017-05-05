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

	private static Planner planner;
	
	private int 	currentStep, 
					nextStep;
	private Cell 	tracked;

	public SimulationWorldModel(int step, Cell tracked)
	{
		this(planner.getModel(step), step, tracked);
	}

	public SimulationWorldModel(GridWorldModel model, int step, Cell tracked)
	{
		super(model);
		
		this.currentStep 	= step - 1;
		this.nextStep		= step;
		this.tracked  		= new Cell(tracked);
	}
	
	public static void setPlanner(Planner planner)
	{
		SimulationWorldModel.planner = planner;
	}
	
	public Location getTrackedLocation()
	{
		return tracked.getLocation();
	}
	
	public int getStep()
	{
		return nextStep;
	}
	
	public void move(int obj, Location fr, Location to)
	{
		if (tracked.getLocation().equals(fr))
		{
			tracked.setLocation(to);
		}
		
		super.move(obj, fr, to);
	}
    
    public SimulationWorldModel run(Action action)
    {    	
    	SimulationWorldModel simulation = new SimulationWorldModel(this, nextStep + 1, this.tracked);
    	
    	for (Agent agent : WorldModel.getInstance().getAgents())
    	{
    		if (planner.getActions().get(agent.getNumber()).size() > nextStep)
    		{
    			Action otherAction = planner.getActions().get(agent.getNumber()).get(nextStep);
    			
    			simulation.doExecute(otherAction);
    		}
    	}
    	
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
        
        if (planner.hasModel(nextStep) && !planner.getModel(nextStep).isFree(nAgLoc)) return false;
        
        if (planner.hasAgentWithOppositeAction(currentStep, action)) return false;

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
        
        if (planner.hasModel(nextStep) && !planner.getModel(nextStep).isFree(nBoxLoc)) return false;
    	
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
        
        if (planner.hasModel(nextStep) && !planner.getModel(nextStep).isFree(nAgLoc)) return false;

        return true;
    }
}
