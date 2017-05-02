package env.model;

import java.util.logging.Logger;

import jason.environment.grid.Location;
import level.Direction;
import level.Actions.Action;
import level.Actions.MoveAction;
import level.Actions.PullAction;
import level.Actions.PushAction;

public class GridWorldModel {
	
	protected static final Logger logger = Logger.getLogger(WorldModel.class.getName());
	
    public static final int 	AGENT   =  1;
	public static final int		GOAL 	=  2;
	public static final int		BOX		=  4;
    public static final int 	WALL 	=  8;
    public static final int 	LOCKED  = 16;
	
	protected int				width, height;
    
    private int[][]             data;
	
	public GridWorldModel(int width, int height)
	{
		this.width 	= width;
		this.height	= height;
		
		this.data 	= new int[width][height];
	}
	
	public GridWorldModel(int[][] data)
	{
		this.width 	= data.length;
		this.height = data[0].length;
		
		this.data 	= data;
	}
    
    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }
    
    public int[][] getDate()
    {
    	return this.data;
    }
    
    public void lock(Location location)
    {
    	add(LOCKED, location);
    }
    
    public void unlock(Location location)
    {
    	remove(LOCKED, location);
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
        return inGrid(x, y) && (data[x][y] & (WALL | AGENT | BOX)) == 0;
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
	
	public void move(int obj, Location fr, Location to)
	{		
		remove	(obj, fr);
		add		(obj, to);
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

	public boolean canExecute(Action action, Location agLoc) 
	{		
        switch(action.getType())
        {
        case MOVE: return canMove((MoveAction) action, agLoc);
        case PUSH: return canPush((PushAction) action, agLoc);
        case PULL: return canPull((PullAction) action, agLoc);
        case SKIP: return true;
        }
        throw new UnsupportedOperationException("Invalid action: " + action.getType());        
	}

    public synchronized boolean canMove(MoveAction action, Location agLoc) 
    {
    	Direction dir = action.getDirection();
    	
//        if (agId < 0) 
//        {
//            logger.warning("** Invalid agent number: " + agId);            
//            return false;
//        }
//    	
//        Location agLoc = getAgPos(agId);
        
        if (agLoc == null) 
        {
            logger.warning("** Lost the location of agent");
            return false;
        }
        
        Location nAgLoc = Direction.newLocation(dir, agLoc);
        
        if (nAgLoc == null)
        {
            logger.warning("** Invalid direction: " + dir);
            return false;
        }
        
        if (!isFree(nAgLoc))
        {
            logger.warning("** Location not free: " + nAgLoc);   
            return false;
        }

        return true;
    }	
    
    public boolean canPush(PushAction action, Location agLoc)
    {
    	Direction dir1 = action.getAgentDir();
    	Direction dir2 = action.getBoxDir();
    	
//        if (agId < 0) 
//        {
//            logger.warning("** Invalid agent number: " + agId);            
//            return false;
//        }
//    	
//        Location agLoc = getAgPos(agId);
        
        if (agLoc == null) 
        {
            logger.warning("** Lost the location of agent");          
            return false;
        }
        
    	Location nAgLoc = Direction.newLocation(dir1, agLoc);
        
        if (nAgLoc == null)
        {
            logger.warning("** Invalid direction: " + dir1);
            return false;
        }
    	
    	if (isFree(BOX, nAgLoc))
    	{
            logger.warning("** No box at: " + nAgLoc);            
            return false;
    	}
    	
    	Location nBoxLoc = Direction.newLocation(dir2, nAgLoc);
        
        if (nBoxLoc == null)
        {
            logger.warning("** Invalid direction: " + dir2);              
            return false;
        }
        
        if (!isFree(nBoxLoc))
        {
            logger.warning("** Location not free: " + nBoxLoc);   
            return false;        	
        }
    	
		return true;
    }
    
    public boolean canPull(PullAction action, Location agLoc)
    {
    	Direction dir1 = action.getAgentDir();
    	Direction dir2 = action.getBoxDir();
    	
//        if (agId < 0) 
//        {
//            logger.warning("** Invalid agent number: " + agId);            
//            return false;
//        }
//    	
//        Location agLoc = getAgPos(agId);
        
        if (agLoc == null) 
        {
            logger.warning("** Lost the location of agent");            
            return false;
        }
        
    	Location boxLoc = Direction.newLocation(dir2, agLoc);
    	
    	if (boxLoc == null)
    	{
            logger.warning("** Invalid direction: " + dir2);              
            return false;
    	}
    	
    	if (isFree(BOX, boxLoc))
    	{
            logger.warning("** No box at: " + boxLoc);            
            return false;
    	}
    	
    	Location nAgLoc = Direction.newLocation(dir1, agLoc);
    	
    	if (nAgLoc == null)
    	{
            logger.warning("** Invalid direction: " + dir1);              
            return false;
    	}
    	
    	if (!isFree(nAgLoc))
    	{
            logger.warning("** Location not free: " + nAgLoc);   
            return false;        	
    	}

        return true;
    }
	
	public void doExecute(Action action, Location agLoc)
	{
        switch(action.getType())
        {
        case MOVE: doMove((MoveAction) action, agLoc); return;
        case PUSH: doPush((PushAction) action, agLoc); return;
        case PULL: doPull((PullAction) action, agLoc); return;	
		case SKIP:                                     return;
        }
        throw new UnsupportedOperationException("Invalid action: " + action.getType());    
	}
    
    public void doMove(MoveAction action, Location agLoc)
    {
    	Direction 	dir 	= action.getDirection();
    	
        Location 	nAgLoc 	= Direction.newLocation(dir, agLoc);
    	
        move(AGENT, agLoc, nAgLoc);
        remove(LOCKED, agLoc.x, agLoc.y);
    }
    
    public void doPush(PushAction action, Location agLoc)
    {
    	Direction 	dir1 	= action.getAgentDir();
    	Direction 	dir2 	= action.getBoxDir();

    	Location 	nAgLoc 	= Direction.newLocation(dir1, agLoc);
    	Location 	nBoxLoc = Direction.newLocation(dir2, nAgLoc);

        move(AGENT, agLoc, nAgLoc);
        move(BOX, nAgLoc, nBoxLoc);
        remove(LOCKED, agLoc.x, agLoc.y);
    }
    
    public void doPull(PullAction action, Location agLoc)
    {
    	Direction dir1 = action.getAgentDir();
    	Direction dir2 = action.getBoxDir();
    	      
    	Location boxLoc = Direction.newLocation(dir2, agLoc);    	
    	Location nAgLoc = Direction.newLocation(dir1, agLoc);

    	move(AGENT, agLoc, nAgLoc);
    	move(BOX, boxLoc, agLoc);
    	remove(LOCKED, boxLoc.x, boxLoc.y);
    }
}
