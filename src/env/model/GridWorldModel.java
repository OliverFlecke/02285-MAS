package env.model;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import level.Location;
import level.action.*;
import level.Color;
import level.Direction;

public class GridWorldModel {
	
	protected static final Logger logger = Logger.getLogger(WorldModel.class.getName());
	
    public static final int 	AGENT   =  1;
	public static final int		GOAL 	=  2;
	public static final int		BOX		=  4;
    public static final int 	WALL 	=  8;
    public static final int 	LOCKED 	= 16;

    public static final int 	TYPE_MASK	= 0xFF;
    public static final int 	COLOR_MASK	= 0xFF00;
    public static final int		GOAL_MASK 	= 0xFF0000;
    public static final int		BOX_MASK 	= 0xFF000000;
	
	protected int					width, height;
    
    private int[][]             	data;
    
    private static Set<Location> 	goalLocations;
    
	
	public GridWorldModel(int width, int height)
	{
		this.width 	= width;
		this.height	= height;
		
		this.data 	= new int[width][height];
	}
	
	/**
	 * Copy constructor
	 * @param model
	 */
	public GridWorldModel(GridWorldModel model)
	{		
		this.data 	= model.deepCopyData();
		
		this.width 	= data.length;
		this.height = data[0].length;
	}
	
	protected void setGoalLocations(Set<Location> locations)
	{
		goalLocations = locations;
	}
    
    public boolean inGrid(int x, int y) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    public boolean hasObject(int obj, Location l) {
        return hasObject(obj, l.x, l.y);
    }
    
    public boolean hasObject(int obj, int x, int y) {
        return inGrid(x, y) && (data[x][y] & obj) != 0;
    }

    public boolean isFree(Location l) {
        return isFree(l.x, l.y);
    }

    public boolean isFree(int x, int y) {
        return inGrid(x, y) && (data[x][y] & (WALL | AGENT | BOX | LOCKED)) == 0;
    }

    public boolean isFree(int obj, Location l) {
        return isFree(obj, l.x, l.y);
    }
    
    public boolean isFree(int obj, int x, int y) {
    	return inGrid(x, y) && (data[x][y] & obj) == 0;
    }
    
    protected void add(int obj, Location l) {
        add(obj, l.x, l.y);
    }

    protected void add(int obj, int x, int y) {
        data[x][y] |= obj;
    }

    protected void remove(int obj, Location l) {
        remove(obj, l.x, l.y);
    }

    protected void remove(int obj, int x, int y) {
        data[x][y] &= ~obj;
    }
    
    protected void addColor(Color color, int x, int y)
    {
    	add(Color.getValue(color) << 8, x , y);
    }
    
    protected void addLetter(char letter, int obj, int x, int y) 
    {
    	int ch = 0;
		
		if ((obj & GOAL) != 0)
		{
			ch = ((int) letter) << 16;
		}
		else if ((obj & BOX) != 0)
		{
			ch = ((int) letter) << 24;
		}
		
		add(ch, x, y);
    }
	
	private int getMasked(int mask, Location l)	{
		return getMasked(mask, l.x, l.y);
	}
	
	private int getMasked(int mask, int x, int y) {
		return data[x][y] & mask;
	}
	
	public void move(int obj, Location fr, Location to)
	{
		obj |= LOCKED;
		
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
			obj |= getMasked(COLOR_MASK, fr);			
		}
				
		remove	(obj, fr);
		add		(obj, to);
	}
	
	public boolean isSolved(Location l) {
		return isSolved(l.x, l.y);
	}
	
	public boolean isSolved(int x, int y) {
		return (data[x][y] & GOAL_MASK) == ((data[x][y] & BOX_MASK) >> 8);
	}
	
	public long countUnsolvedGoals()
	{		
		return goalLocations.stream().filter(goal -> !isSolved(goal)).count();
	}
	
	public void printLevel()
	{
		// Print integer representation of level
		for (int y = 0; y < height; y++) 
		{
			for (int x = 0; x < width; x++) 
			{
				System.err.print(data[x][y]);
			}
			System.err.println();
		}
	}
	
	public int[][] deepCopyData() 
	{
	    int[][] result = new int[data.length][];
	    
	    for (int row = 0; row < data.length; row++) 
	    {
	        result[row] = data[row].clone();
	    }
	    return result;
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

        return true;
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
        throw new UnsupportedOperationException("Invalid action: " + action.getType());    
	}
    
    public void doMove(MoveAction action)
    {    	
    	Direction 	dir 	= action.getDirection();

    	Location 	agLoc 	= action.getAgentLocation();
        Location 	nAgLoc 	= Location.newLocation(dir, agLoc);
    	
        move(AGENT, agLoc, nAgLoc);
    }
    
    public void doPush(PushAction action)
    {    	
    	Direction 	dir1 	= action.getAgentDir();
    	Direction 	dir2 	= action.getBoxDir();

    	Location 	agLoc 	= action.getAgentLocation();
    	Location 	boxLoc 	= Location.newLocation(dir1, agLoc);
    	Location 	nBoxLoc = Location.newLocation(dir2, boxLoc);

        move(AGENT, agLoc, boxLoc);
        move(BOX, boxLoc, nBoxLoc);
    }
    
    public void doPull(PullAction action)
    {
    	Direction 	dir1 	= action.getAgentDir();
    	Direction 	dir2 	= action.getBoxDir();

    	Location 	agLoc  	= action.getAgentLocation();
    	Location 	boxLoc 	= Location.newLocation(dir2, agLoc);    	
    	Location 	nAgLoc 	= Location.newLocation(dir1, agLoc);

    	move(AGENT, agLoc, nAgLoc);
    	move(BOX, boxLoc, agLoc);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridWorldModel other = (GridWorldModel) obj;
		if (!Arrays.deepEquals(data, other.data))
			return false;
		return true;
	}
}
