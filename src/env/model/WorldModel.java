package env.model;

import java.util.*;
import level.Location;
import level.action.*;
import level.Level;
import level.Color;
import level.Direction;

public class WorldModel extends DataWorldModel {
	
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
				
				if (Character.isDigit(ch)) addAgent(x, y, ch, Color.getColor(colors.get(ch)));
				
				else if (Character.isLowerCase(ch)) addGoal(x, y, ch);
				
				else if (Character.isUpperCase(ch)) addBox(x, y, ch, Color.getColor(colors.get(ch)));
				
				else if (ch == '+') addWall(x, y);
			}
		}
		printLevel();
	}
    
    public void doMove(MoveAction action, int agId)
    {
    	Direction 	dir 	= action.getDirection();
    	
        Location 	agLoc 	= getAgPos(agId);
        Location 	nAgLoc 	= Location.newLocation(dir, agLoc);
    	
        move(AGENT, agLoc, nAgLoc);
        remove(LOCKED, agLoc.x, agLoc.y);
    }
    
    public void doPush(PushAction action, int agId)
    {
    	Direction 	dir1 	= action.getAgentDir();
    	Direction 	dir2 	= action.getBoxDir();

        Location 	agLoc 	= getAgPos(agId);
    	Location 	nAgLoc 	= Location.newLocation(dir1, agLoc);
    	Location 	nBoxLoc = Location.newLocation(dir2, nAgLoc);

        move(AGENT, agLoc, nAgLoc);
        move(BOX, nAgLoc, nBoxLoc);
        remove(LOCKED, agLoc.x, agLoc.y);
    }
    
    public void doPull(PullAction action, int agId)
    {
    	Direction dir1 = action.getAgentDir();
    	Direction dir2 = action.getBoxDir();
    	
        Location agLoc 	= getAgPos(agId);        
    	Location boxLoc = Location.newLocation(dir2, agLoc);    	
    	Location nAgLoc = Location.newLocation(dir1, agLoc);

    	move(AGENT, agLoc, nAgLoc);
    	move(BOX, boxLoc, agLoc);
    	remove(LOCKED, boxLoc.x, boxLoc.y);
    }
}
