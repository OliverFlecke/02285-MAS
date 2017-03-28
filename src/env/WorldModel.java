package env;

import java.util.logging.Logger;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class WorldModel extends GridWorldModel {
	
	private static final Logger logger = Logger.getLogger(WorldModel.class.getName());

	// GridWorldModel uses bit flags to represent objects
	//						CLEAN		=  0
	//						AGENT		=  2
	//						OBSTACLE 	=  4
	public static final int	GOAL 		=  8;
	public static final int	BOX 		= 16;
	
	/**
	 * Constructs a new GridWorldModel.
	 * @param w - Width
	 * @param h - Height
	 * @param nbAgs - Number of agents
	 */
	public WorldModel(int w, int h, int nbAgs) {
		super(w, h, nbAgs); 
	}
	
	/**
	 * Initializes the grid with objects according to the 
	 * character.
	 * @param level - Two-dimensional char array
	 */
	protected void init(char[][] level) 
	{		
		for (int i = 0; i < width; i++) 
		{
			for (int j = 0; j < height; j++) 
			{
				char ch = level[i][j];
				
				if (Character.isDigit(ch)) 
					agPos[Character.getNumericValue(ch)] = new Location(i, j);
				
				else if (ch == '+') add(OBSTACLE, i, j);
				
				else if (Character.isUpperCase(ch)) add(BOX , i, j);
				
				else if (Character.isLowerCase(ch)) add(GOAL, i, j);
			}
		}

		for (int i = 0; i < width; i++) 
		{
			for (int j = 0; j < height; j++) 
			{
				System.out.print(level[i][j]);
			}
			System.out.println();
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
    
    public static enum Direction {
        N, S, E, W
    };
    
    /**
     * Computes a new Location based on current direction 
     * and location.
     * @param dir - Direction
     * @param l - Location
     * @return The new Location.
     */
    public static Location newLocation(Direction dir, Location l)
    {        
        switch (dir) 
        {
        case N: return new Location(l.x, l.y - 1);
        case S: return new Location(l.x, l.y + 1);
        case E: return new Location(l.x + 1, l.y);
        case W: return new Location(l.x - 1, l.y);
        }        
        return l;
    }
    
    /**
     * Computes a new Location based on current direction
     * and the agent, doing proper error checks.
     * @param dir - Direction
     * @param agId - Agent ID
     * @return The new Location
     */
    public Location newLocation(Direction dir, int agId)
    {
        if (agId < 0) 
        {
            logger.warning("** Unknown agent number: " + agId);            
            return null;
        }
        
        Location l = getAgPos(agId);
        
        if (l == null) 
        {
            logger.warning("** Lost the location of agent " + (agId + 1));            
            return null;
        }
    	
        return newLocation(dir, l);
    }

    /**
     * Moves an agent based on the direction.
     * @param dir - Direction
     * @param agId - Agent ID
     * @return True if and only if action succeeds.
     */
    synchronized public boolean move(Direction dir, int agId) 
    {        
        Location nAgLoc = newLocation(dir, agId);
        
        if (nAgLoc != null && isFree(nAgLoc)) 
        {
        	setAgPos(agId, nAgLoc);
            return true;
        }
        return false;
    }	
    
    /**
     * Pushes a box based on the direction of the agent
     * and the direction of the box?
     * @param dir1 - Direction of the agent
     * @param dir2 - Direction of the box
     * @param agId - Agent ID
     * @return True if and only if the action succeeds.
     */
    synchronized public boolean push(Direction dir1, Direction dir2, int agId)
    {
    	Location nAgLoc = newLocation(dir1, agId);
    	
    	if (nAgLoc == null || !hasObject(BOX, nAgLoc))
    	{
            logger.warning("** Trying to push unknown box!");            
            return false;
    	}
    	
    	Location nBoxLoc = newLocation(dir2, nAgLoc);
    	
    	if (nBoxLoc != null && isFree(nBoxLoc))
    	{
    		setAgPos(agId, nAgLoc);
    		remove(BOX, nAgLoc);
    		add(BOX, nBoxLoc);
    		return true;
    	}    	
    	return false;
    }
    
    /**
     * Pulls a box based on the direction of the agent 
     * and the direction of the box?
     * @param dir1 - Direction of the agent
     * @param dir2 - Direction of the box
     * @param agId - Agent ID
     * @return True if and only if the action succeeds.
     */
    synchronized public boolean pull(Direction dir1, Direction dir2, int agId)
    {    	
    	Location cAgLoc = getAgPos(agId);
    	
    	Location cBoxLoc = newLocation(dir2, cAgLoc);
    	
    	if (cBoxLoc == null || !hasObject(BOX, cBoxLoc))
    	{
            logger.warning("** Trying to pull unknown box!");            
            return false;
    	}
    	
    	Location nAgLoc = newLocation(dir1, agId);
    	
    	if (nAgLoc != null && isFree(nAgLoc))
    	{            
    		setAgPos(agId, nAgLoc);
    		remove(BOX, cBoxLoc);
    		add(BOX, cAgLoc);
            return true;
    	}    	
    	return false;
    }
    
}
