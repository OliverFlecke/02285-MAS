package env;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jason.asSyntax.Term;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import lvl.cell.*;

public class WorldModel extends GridWorldModel {
	
	private static final Logger logger = Logger.getLogger(WorldModel.class.getName());
	
	private static WorldModel instance;
	
	private Agent[]				agents;
	private Set<Box>			boxes	= new HashSet<>();
	private Set<Goal>			goals	= new HashSet<>();
	
	private Agent[][]			agentArray;
	private Box[][]				boxArray;
	private Goal[][]			goalArray;

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
	public WorldModel(lvl.Level level) 
	{		
		super(level.width, level.height, level.nbAgs);

		agents 		= new Agent[level.nbAgs];
		
		agentArray 	= new Agent[width][height];
		boxArray  	= new Box  [width][height];
		goalArray 	= new Goal [width][height];
		
		initData(level.data, level.colors);
		
		instance = this;
	}
	
	public static WorldModel getInstance() {
		return instance;
	}

	@Override
    public int getNbOfAgs() {
        return agents.length;
    }
	
	@Override
	public Location getAgPos(int agId) {
		return agents[agId].getLocation();
	}
	
	@Override
	public void setAgPos(int agId, Location l) {
		move(AGENT, agents[agId].getLocation(), l);
	}
	
	public Agent[] getAgents()
	{
		return this.agents;
	}
	
	/**
	 * @return The data in this world model
	 */
	public int[][] getData()
	{
		return this.data;
	}
	
	/**
	 * @return The array of all the goals
	 */
	public Goal[][] getGoalsArray()
	{
		return this.goalArray;
	}
	
	/**
	 * @return The goals in the world model
	 */
	public Set<Goal> getGoals()
	{
		return this.goals;
	}
	
	/**
	 * @return The box array in the world model
	 */
	public Box[][] getBoxArray()
	{
		return this.boxArray;
	}
	
	/**
	 * @return The boxes in the world
	 */
	public Set<Box> getBoxes()
	{
		return this.boxes;
	}
	
	/**
	 * @return All the boxes in the world, which is not already on a goal
	 */
	public Set<Box> getBoxesNotOnGoal()
	{
		return boxes.stream().filter(box -> !box.onGoal()).collect(Collectors.toSet());
	}
	
	/**
	 * @return Get all the goals that has not been solved yet
	 */
	public Set<Goal> getUnsolvedGoals()
	{
		return goals.stream().filter(goal -> !goal.isSolved()).collect(Collectors.toSet());
	}
	
	public void move(int value, Location fr, Location to)
	{
		switch (value)
		{
		case AGENT: 
			agentArray[fr.x][fr.y].setLocation(to); 
			agentArray[to.x][to.y] = agentArray[fr.x][fr.y];
			agentArray[fr.x][fr.y] = null;
			break;
		case BOX:	
			boxArray  [fr.x][fr.y].setLocation(to); 
			boxArray  [to.x][to.y] = boxArray[fr.x][fr.y];
			boxArray  [fr.x][fr.y] = null;
			break;
		default: 	return;
		}

		remove	(value, fr);
		add		(value, to);
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
				
				if (Character.isDigit(ch)) 
				{
					int number = Character.getNumericValue(ch);
					Agent agent = new Agent(x, y, ch, colors.getOrDefault(ch, ""));

					add(AGENT, x, y);			// Add to integer representation
					agents[number] = agent;		// Add to map for quick lookup
					agentArray[x][y] = agent;	// Add to array for quick lookup
				}
				
				else if (ch == '+') add(OBSTACLE, x, y);
				
				else if (Character.isUpperCase(ch)) 
				{
					Box box = new Box(x, y, ch, colors.getOrDefault(ch, ""));
					
					add(BOX, x, y);				// Add to integer representation
					boxes.add(box);             // Add to set to easily get all
					boxArray[x][y] = box;		// Add to array for quick lookup
				}
				
				else if (Character.isLowerCase(ch)) 
				{
					Goal goal = new Goal(x, y, ch);
					
					add(GOAL, x, y);			// Add to integer representation
					goals.add(goal);            // Add to set to easily get all 
					goalArray[x][y] = goal;		// Add to array for quick lookup
				}
			}
		}
		printLevel();
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

	/**
	 * Adjusted to take boxes into account.
	 */
	@Override
	public boolean isFree(int x, int y) {
        return super.isFree(x, y) && !hasObject(BOX, x, y);
    }
	
	/**
	 * Should agents be considered?
	 */
	public boolean noWallsOrBoxes(Location l) {
		return inGrid(l) && isFree(OBSTACLE, l) && isFree(BOX, l);
	}
	
	/* 
	 *  ACTIONS
	 */
	
	public static final String UP    = "up"   ;
	public static final String DOWN  = "down" ;
	public static final String LEFT  = "left" ;
	public static final String RIGHT = "right";
	public static final String[] DIRECTIONS = {
			UP, DOWN, LEFT, RIGHT
	};

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

    	move(AGENT, agLoc, nAgLoc);
    	move(BOX, boxLoc, agLoc);
        return true;
    }    
}
