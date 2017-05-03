package env.model;

import java.util.*;

import level.Level;
import level.Color;

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
}
