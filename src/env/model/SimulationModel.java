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

public class SimulationModel extends ActionModel {

	private static Planner planner;
	
	private int 	currentStep, 
					nextStep;
	private Agent	agent;
	private Cell 	tracked;
	private boolean isAgent;

	public SimulationModel(int step, Agent agent, Cell tracked)
	{
		this(planner.getModel(step), step, agent, tracked);
	}
	
	public SimulationModel(DataModel model, int step, Agent agent, Cell tracked)
	{
		this(model, step, agent, tracked, tracked instanceof Agent);
	}

	public SimulationModel(DataModel model, int step, Agent agent, Cell tracked, boolean isAgent)
	{
		super(model);
		
		this.currentStep 	= step;
		this.nextStep		= step + 1;
		this.agent			= new Agent(agent);
		this.tracked  		= new Cell(tracked);		
		this.isAgent 		= isAgent;
	}
	
	public static void setPlanner(Planner planner)
	{
		SimulationModel.planner = planner;
	}
	
	public Location getTrackedLocation()
	{
		return tracked.getLocation();
	}
	
	public boolean isTrackedAgent()
	{
		return isAgent;
	}
	
	public Agent getAgent()
	{
		return agent;
	}
	
	public int getStep()
	{
		return currentStep;
	}
	
	public void move(int obj, Location fr, Location to)
	{
		if (tracked.getLocation().equals(fr))
		{
			tracked.setLocation(to);
		}		
		super.move(obj, fr, to);
	}
    
    public SimulationModel run(Action action)
    {    	
    	SimulationModel simulation = new SimulationModel(this, nextStep, agent, tracked, isAgent);
    	
    	for (Agent agent : WorldModel.getInstance().getAgents())
    	{
    		if (currentStep - 1 < planner.getActions().get(agent.getNumber()).size())
    		{
    			Action otherAction = planner.getActions().get(agent.getNumber()).get(currentStep - 1);
    			
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
        
        Location nAgLoc = agLoc.newLocation(dir);
        
        if (nAgLoc == null) return false;
        
        if (!isFree(nAgLoc)) return false;
        
        if (planner.hasModel(currentStep) && !planner.getModel(currentStep).isFree(nAgLoc)) return false;
        
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
        
    	Location boxLoc = agLoc.newLocation(dir1);
        
        if (boxLoc == null) return false;
    	
    	if (isFree(BOX, boxLoc)) return false;
        
        int boxColor = getMasked(COLOR_MASK, boxLoc);
        
        if (agColor != boxColor) return false;
    	
    	Location nBoxLoc = boxLoc.newLocation(dir2);
        
        if (nBoxLoc == null) return false;
        
        if (!isFree(nBoxLoc)) return false;
        
        if (planner.hasModel(currentStep) && !planner.getModel(currentStep).isFree(nBoxLoc)) return false;
        
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
        
    	Location boxLoc = agLoc.newLocation(dir2);
    	
    	if (boxLoc == null) return false;
    	
    	if (isFree(BOX, boxLoc)) return false;
    	
    	int boxColor = getMasked(COLOR_MASK, boxLoc);
    	
    	if (agColor != boxColor) return false;
    	
    	Location nAgLoc = agLoc.newLocation(dir1);
    	
    	if (nAgLoc == null) return false;
        
        if (!isFree(nAgLoc)) return false;
        
        if (planner.hasModel(currentStep) && !planner.getModel(currentStep).isFree(nAgLoc)) return false;
        
        if (planner.hasModel(nextStep) && !planner.getModel(nextStep).isFree(nAgLoc)) return false;

        return true;
    }
}
