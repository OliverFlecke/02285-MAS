package env.model;

import env.planner.Planner;
import level.Direction;
import level.Location;
import level.action.Action;
import level.action.MoveAction;
import level.action.PullAction;
import level.action.PushAction;
import level.cell.Cell;

public class SimulationWorldModel extends GridWorldModel {
	
	private int step;
	private Cell tracked;
	private static Planner planner;
	private boolean isUpdated = false;

	public SimulationWorldModel(GridWorldModel model, int step, Cell tracked)
	{
		super(model);
		
		this.step		= step;
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
	
	public int getStep()
	{
		return step;
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
    	SimulationWorldModel simulation = new SimulationWorldModel(this, step + 1, this.tracked);
    	
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
        
        if (!isFree(WALL, nAgLoc)) return false;
        
        if (planner.hasModel(step - 1) && !planner.getModel(step - 1).isFree(nAgLoc)) return false;
        
        if (planner.hasModel(step) && !planner.getModel(step).isFree(nAgLoc)) return false;
        
        if (planner.hasAgentWithOppositeAction(step - 1, action)) return false;

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
        
        if (!isFree(WALL, nBoxLoc)) return false;
        
        if (planner.hasModel(step - 1) && !planner.getModel(step - 1).isFree(nBoxLoc)) return false;
        
        if (planner.hasModel(step) && !planner.getModel(step).isFree(nBoxLoc)) return false;
    	
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
        
        if (!isFree(WALL, nAgLoc)) return false;
        
        if (planner.hasModel(step - 1) && !planner.getModel(step - 1).isFree(nAgLoc)) return false;
        
        if (planner.hasModel(step) && !planner.getModel(step).isFree(nAgLoc)) return false;

        return true;
    }
    
    @Override
    public boolean equals(Object obj) 
    {
    	if (planner.hasModel(step))
    	{
    		return false;
    	}
    	else if (!isUpdated)
    	{
    		GridWorldModel model = planner.getModel(step - 1);
    		
    		this.deepAddData(model);
    		
    		isUpdated = true;
    	}
    	return super.equals(obj);
    }
}
