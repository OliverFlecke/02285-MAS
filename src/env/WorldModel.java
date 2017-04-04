package env;

import java.util.logging.Logger;
import jason.asSyntax.Term;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class WorldModel extends GridWorldModel {
	
	private static final Logger logger = Logger.getLogger(WorldModel.class.getName());

	// GridWorldModel uses bit flags to represent objects
	//						CLEAN		= 0
	public static final int	GOAL 		= 1;
	//						AGENT		= 2
	//						OBSTACLE 	= 4
	public static final int	BOX 		= 8;
	
	/**
	 * Constructs a new WorldModel based on a level.
	 * @param level - Level object
	 */
	public WorldModel(Level level) 
	{
		super(level.width, level.height, level.nbAgs); 
		
		initData(level.data);
	}
	
	/**
	 * Initializes the grid with objects according to the 
	 * data.
	 * @param data - Two-dimensional char array
	 */
	private void initData(char[][] data) 
	{
		// Print character representation of level
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				System.err.print(data[i][j]);
			}
			System.err.println();
		}
		
		for (int i = 0; i < width; i++) 
		{
			for (int j = 0; j < height; j++) 
			{
				char ch = data[j][i];
				
				if (Character.isDigit(ch)) 
				{
					add(AGENT, i, j);
					agPos[Character.getNumericValue(ch)] = new Location(i, j);
				}
				
				else if (ch == '+') add(OBSTACLE, i, j);
				
				else if (Character.isUpperCase(ch)) add(BOX , i, j);
				
				else if (Character.isLowerCase(ch)) add(GOAL, i, j);
			}
		}

		// Print integer representation of level
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				System.err.print(this.data[j][i]);
			}
			System.err.println();
		}
	}

	/**
	 * Adjusted to take boxes into account.
	 */
	@Override
	public boolean isFree(int x, int y) {
        return super.isFree(x, y) && !hasObject(BOX, x, y);
    }
	
	/* 
	 *  ACTIONS
	 */
	
	public static final String UP    = "up"   ;
	public static final String DOWN  = "down" ;
	public static final String LEFT  = "left" ;
	public static final String RIGHT = "right";

	/**
	 * Computes a new Location based on current direction 
	 * and location.
	 * @param dir - Direction
	 * @param l - Location
	 * @return The new Location.
	 */
	public static Location newLocation(Term dir, Location l)
	{        
	    switch (dir.toString()) 
	    {
	    case UP   : return new Location(l.x, l.y - 1);
	    case DOWN : return new Location(l.x, l.y + 1);
	    case LEFT : return new Location(l.x + 1, l.y);
	    case RIGHT: return new Location(l.x - 1, l.y);
	    }        
	    return null; // Could return l
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

    	setAgPos(agId, nAgLoc);
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
    	
    	if (!hasObject(BOX, nAgLoc))
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
    	
		setAgPos(agId, nAgLoc);
		remove(BOX, nAgLoc);
		add(BOX, nBoxLoc);
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
    	
    	if (!hasObject(BOX, boxLoc))
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

		setAgPos(agId, nAgLoc);
		remove(BOX, boxLoc);
		add(BOX, agLoc);
        return true;
    }    
}
