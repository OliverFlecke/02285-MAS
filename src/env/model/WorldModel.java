package env.model;

import java.util.*;
import java.util.logging.Logger;

import jason.asSyntax.Term;
import jason.environment.grid.Location;
import lvl.Level;

public class WorldModel extends DataWorldModel {
	
	private static final Logger logger = Logger.getLogger(WorldModel.class.getName());
	
	public static final String UP    = "up"   ;
	public static final String DOWN  = "down" ;
	public static final String LEFT  = "left" ;
	public static final String RIGHT = "right";
	public static final String[] DIRECTIONS = {
			UP, DOWN, LEFT, RIGHT
	};
	
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

	/**
	 * Computes a new Location based on current direction 
	 * and location.
	 * @param dir - Direction
	 * @param l - Location
	 * @return The new Location.
	 */
	public static Location newLocation(String dir, Location l)
	{        
	    switch (dir) 
	    {
	    case UP   : return new Location(l.x, l.y - 1);
	    case DOWN : return new Location(l.x, l.y + 1);
	    case LEFT : return new Location(l.x - 1, l.y);
	    case RIGHT: return new Location(l.x + 1, l.y);
	    }        
	    return null; // Could return l
	}
	
	public static Location newLocation(Term dir, Location l) {
		return newLocation(dir.toString(), l);
	}

    /**
     * Moves an agent based on the direction.
     * @param dir - Direction
     * @param agId - Agent ID
     * @return True if and only if action succeeds.
     */
    public synchronized boolean move(Term dir, int agId) 
    {        
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
        
        Location nAgLoc = newLocation(dir, agLoc);
        
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

        move(AGENT, agLoc, nAgLoc);
        remove(LOCKED, agLoc.x, agLoc.y);
        return true;
    }	
    
    /**
     * Pushes a box based on the direction of the agent
     * and the direction of the box?
     * @param dir1 - Direction of the agent
     * @param dir2 - Direction of the box
     * @param agId - Agent ID
     * @return True if and only if the action succeeds.
     */
    public synchronized boolean push(Term dir1, Term dir2, int agId)
    {
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
        
    	Location nAgLoc = newLocation(dir1, agLoc);
        
        if (nAgLoc == null)
        {
            logger.warning("** Invalid direction: " + dir1);
            return false;
        }
    	
    	if (isFree(nAgLoc, BOX))
    	{
            logger.warning("** No box at: " + nAgLoc);            
            return false;
    	}
    	
    	Location nBoxLoc = newLocation(dir2, nAgLoc);
        
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
    	
        move(AGENT, agLoc, nAgLoc);
        move(BOX, nAgLoc, nBoxLoc);
		return true;
    }
    
    /**
     * Pulls a box based on the direction of the agent 
     * and the direction of the box?
     * @param dir1 - Direction of the agent
     * @param dir2 - Direction of the box
     * @param agId - Agent ID
     * @return True if and only if the action succeeds.
     */
    public synchronized boolean pull(Term dir1, Term dir2, int agId)
    {    	
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
        
    	Location boxLoc = newLocation(dir2, agLoc);
    	
    	if (boxLoc == null)
    	{
            logger.warning("** Invalid direction: " + dir2);              
            return false;
    	}
    	
    	if (isFree(boxLoc, BOX))
    	{
            logger.warning("** No box at: " + boxLoc);            
            return false;
    	}
    	
    	Location nAgLoc = newLocation(dir1, agLoc);
    	
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

    	move(AGENT, agLoc, nAgLoc);
    	move(BOX, boxLoc, agLoc);
        return true;
    }    
}
