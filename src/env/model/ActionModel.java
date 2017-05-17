package env.model;

import java.util.Set;

import level.Location;
import level.action.Action;
import level.action.MoveAction;
import level.action.PullAction;
import level.action.PushAction;

public class ActionModel extends DataModel {
    
    private static Set<Location> goalLocations;

	public ActionModel(DataModel model) {
		super(model);
	}

	public ActionModel(int width, int height) {
		super(width, height);
	}

	public ActionModel(int[][] data) {
		super(data);
	}
	
	protected void setGoalLocations(Set<Location> locations) {
		goalLocations = locations;
	}
	
	public int countUnsolvedGoals() {
		return Math.toIntExact(goalLocations.stream().filter(goal -> !isSolved(goal)).count());
	}
	
	public void doExecute(Action action)
	{
        switch(action.getType())
        {
        case MOVE: doMove((MoveAction) action); return;
        case PUSH: doPush((PushAction) action); return;
        case PULL: doPull((PullAction) action); return;	
		case SKIP:                              return;
        }
        throw new UnsupportedOperationException("Invalid action: " + action);    
	}
    
    public void doMove(MoveAction action)
    {
    	Location 	agLoc 	= action.getAgentLocation();
        Location 	nAgLoc 	= action.getNewAgentLocation();
    	
        move(AGENT, agLoc, nAgLoc);
    }
    
    public void doPush(PushAction action)
    {
    	Location 	agLoc 	= action.getAgentLocation();
    	Location 	nAgLoc 	= action.getNewAgentLocation();
    	Location 	boxLoc 	= action.getBoxLocation();
    	Location 	nBoxLoc = action.getNewBoxLocation();

        move(BOX, boxLoc, nBoxLoc);
        move(AGENT, agLoc, nAgLoc);
    }
    
    public void doPull(PullAction action)
    {
    	Location 	agLoc  	= action.getAgentLocation();
    	Location 	nAgLoc 	= action.getNewAgentLocation();
    	Location 	boxLoc 	= action.getBoxLocation();
    	Location	nBoxLoc = action.getNewBoxLocation();

    	move(AGENT, agLoc, nAgLoc);
    	move(BOX, boxLoc, nBoxLoc);
    }
	
	public void move(int obj, Location fr, Location to)
	{		
		if ((obj & GOAL) != 0)
		{
			obj |= getMasked(GOAL_MASK, fr);
		}
		else if ((obj & BOX) != 0)
		{
			obj |= getMasked(BOX_MASK, fr);
			obj |= getMasked(COLOR_MASK, fr);
		}
		else if ((obj & AGENT) != 0)
		{
			obj |= getMasked(BOX_MASK, fr);
			obj |= getMasked(COLOR_MASK, fr);			
		}
				
		remove	(obj, fr);
		add		(obj, to);
	}

}
