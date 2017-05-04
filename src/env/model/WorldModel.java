package env.model;

import java.util.*;
import java.util.stream.Collectors;

import level.Level;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Goal;
import level.Color;

public class WorldModel extends DataWorldModel {
	
	private Map<Character, Set<Goal>> 	goalMap;
	private Map<Character, Set<Box>>  	boxMap;
	
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
		
		goalMap 	= new HashMap<>();
		boxMap		= new HashMap<>();
		
		initData(level.data, level.colors);
		
		setGoalLocations(goals.stream().map(goal -> goal.getLocation())
									   .collect(Collectors.toSet()));
		
		instance = this;
	}
	
	public static WorldModel getInstance() {
		return instance;
	}
	
	public Map<Character, Set<Goal>> getGoalMap() {
		return goalMap;
	}
	
	public Map<Character, Set<Box>> getBoxMap() {
		return boxMap;
	}
	
	public Set<Goal> getGoals(char letter) {
		return goalMap.get(letter);
	}
	
	public Set<Box> getBoxes(char letter) {
		return boxMap.get(letter);
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
		System.err.println(toString());
	}
	
	protected void addAgent(int x, int y, char letter, Color color) 
	{
		Agent agent = new Agent(x, y, letter, color);
		int number = agent.getNumber();
		
		add(AGENT, x, y);
		addLetter(letter, BOX, x, y);
		addColor(color, x, y);
		agents[number] = agent;
		agentArray[x][y] = agent;
	}
	
	protected void addGoal(int x, int y, char letter) 
	{
		Goal goal = new Goal(x, y, letter);
		
		add(GOAL, x, y);
		addLetter(letter, GOAL, x, y);
		goals.add(goal);
		goalArray[x][y] = goal;
		addToMap(goalMap, letter, goal);
	}
	
	protected void addBox(int x, int y, char upperCaseLetter, Color color) 
	{
		char letter = Character.toLowerCase(upperCaseLetter);
		
		Box box = new Box(x, y, letter, color);

		add(BOX, x, y);
		addLetter(letter, BOX, x, y);
		addColor(color, x, y);
		boxes.add(box);
		boxArray[x][y] = box;
		addToMap(boxMap, letter, box);
	}
	
	protected void addWall(int x, int y)
	{
		add(WALL, x, y);
	}
	
	private static <T> void addToMap(Map<Character, Set<T>> map, char letter, T object)
	{
		if (map.containsKey(letter))
		{
			map.get(letter).add(object);
		}
		else
		{
			map.put(letter, new HashSet<T>(Arrays.asList(object)));
		}
	}
}
