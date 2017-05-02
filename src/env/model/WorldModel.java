package env.model;

import java.util.*;
import java.util.logging.Logger;

import jason.environment.grid.Location;
import level.Level;
import level.Actions.*;
import level.Direction;

public class WorldModel extends DataWorldModel {
	
	private static final Logger logger = Logger.getLogger(WorldModel.class.getName());
	
	private int step = 0;
	
	public int getStep() { return this.step; }
	
	public void nextStep() { step++; }
	
	private static WorldModel instance;
	
	/**
	 * Constructs a new WorldModel based on a level.
	 * @param level - Level object
	 */
	public WorldModel(Level level) 
	{		
		super(level.width, level.height, level.nbAgs);
		
		initData(level.data, level.colors);
		
		instance = this;
	}
	
	public static WorldModel getInstance() {
		return instance;
	}
	
	/**
	 * Initializes the grid with objects according to the 
	 * data.
	 * @param data - Two-dimensional char array
	 */
	private void initData(char[][] data, Map<Character, String> colors) 
	{		
		for (int x = 0; x < width; x++) 
		{
			for (int y = 0; y < height; y++) 
			{
				char ch = data[x][y];
				
				if (Character.isDigit(ch)) addAgent(x, y, ch, colors.get(ch));
				
				else if (Character.isLowerCase(ch)) addGoal(x, y, ch);
				
				else if (Character.isUpperCase(ch)) addBox(x, y, ch, colors.get(ch));
				
				else if (ch == '+') addWall(x, y);
			}
		}
		printLevel();
	}

    public synchronized boolean canMove(MoveAction action, int agId) 
    {
    	Direction dir = action.getDirection();
    	
        if (agId < 0) 
        {
            logger.warning("** Invalid agent number: " + agId);            
            return false;
        }
    	
        Location agLoc = getAgPos(agId);
        
        if (agLoc == null) 
        {
            logger.warning("** Lost the location of agent " + (agId + 1));            
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
    
    public void doMove(MoveAction action, int agId)
    {
    	Direction 	dir 	= action.getDirection();
    	
        Location 	agLoc 	= getAgPos(agId);
        Location 	nAgLoc 	= Direction.newLocation(dir, agLoc);
    	
        move(AGENT, agLoc, nAgLoc);
        remove(LOCKED, agLoc.x, agLoc.y);
    }
    
    public boolean canPush(PushAction action, int agId)
    {
    	Direction dir1 = action.getAgentDir();
    	Direction dir2 = action.getBoxDir();
    	
        if (agId < 0) 
        {
            logger.warning("** Invalid agent number: " + agId);            
            return false;
        }
    	
        Location agLoc = getAgPos(agId);
        
        if (agLoc == null) 
        {
            logger.warning("** Lost the location of agent " + (agId + 1));            
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
    
    public void doPush(PushAction action, int agId)
    {
    	Direction 	dir1 	= action.getAgentDir();
    	Direction 	dir2 	= action.getBoxDir();

        Location 	agLoc 	= getAgPos(agId);
    	Location 	nAgLoc 	= Direction.newLocation(dir1, agLoc);
    	Location 	nBoxLoc = Direction.newLocation(dir2, nAgLoc);

        move(AGENT, agLoc, nAgLoc);
        move(BOX, nAgLoc, nBoxLoc);
        remove(LOCKED, agLoc.x, agLoc.y);
    }
    
    /**
     * Pulls a box based on the direction of the agent 
     * and the direction of the box?
     * @param dir1 - Direction of the agent
     * @param dir2 - Direction of the box
     * @param agId - Agent ID
     * @return True if and only if the action succeeds.
     */
    public boolean canPull(PullAction action, int agId)
    {
    	Direction dir1 = action.getAgentDir();
    	Direction dir2 = action.getBoxDir();
    	
        if (agId < 0) 
        {
            logger.warning("** Invalid agent number: " + agId);            
            return false;
        }
    	
        Location agLoc = getAgPos(agId);
        
        if (agLoc == null) 
        {
            logger.warning("** Lost the location of agent " + (agId + 1));            
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
    
    public void doPull(PullAction action, int agId)
    {
    	Direction dir1 = action.getAgentDir();
    	Direction dir2 = action.getBoxDir();
    	
        Location agLoc 	= getAgPos(agId);        
    	Location boxLoc = Direction.newLocation(dir2, agLoc);    	
    	Location nAgLoc = Direction.newLocation(dir1, agLoc);

    	move(AGENT, agLoc, nAgLoc);
    	move(BOX, boxLoc, agLoc);
    	remove(LOCKED, boxLoc.x, boxLoc.y);
    }
}
