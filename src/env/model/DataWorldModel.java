package env.model;

import java.util.*;
import java.util.stream.Collectors;

import jason.environment.grid.Location;
import level.cell.*;

public class DataWorldModel extends GridWorldModel {
	
	private Agent[]						agents;
	private Set<Goal>					goals;
	private Set<Box>					boxes;

	private Agent[][]					agentArray;
	private Goal[][]					goalArray;
	private Box[][]						boxArray;
	
	private Map<Character, Set<Goal>> 	goalMap;
	private Map<Character, Set<Box>>  	boxMap;
	
	public DataWorldModel(int width, int height, int nbAgs)
	{
		super(width, height);
		
		agents 		= new Agent[nbAgs];
		goals		= new HashSet<>();
		boxes		= new HashSet<>();
		
		agentArray 	= new Agent[width][height];
		goalArray 	= new Goal [width][height];
		boxArray  	= new Box  [width][height];
		
		goalMap 	= new HashMap<>();
		boxMap		= new HashMap<>();
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
    
    public int getNbAgs() {
    	return agents.length;
    }
	
	public Agent getAgent(int i) {
		return agents[i];
	}
	
	public Location getAgPos(int i) {
		return agents[i].getLocation();
	}
	
	public Agent getAgent(Location l) {
		return getAgent(l.x, l.y);
	}
	
	public Goal getGoal(Location l) {
		return getGoal(l.x, l.y);
	}
	
	public Box getBox(Location l) {
		return getBox(l.x, l.y);
	}
	
	public Agent[] getAgents() {
		return agents;
	}
	
	public Set<Goal> getGoals() {
		return goals;
	}
	
	public Set<Box> getBoxes() {
		return boxes;
	}
	
	public Agent getAgent(int x, int y) {
		return agentArray[x][y];
	}
	
	public Goal getGoal(int x, int y) {
		return goalArray[x][y];
	}
	
	public Box getBox(int x, int y) {
		return boxArray[x][y];
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
	
	
	/* OBJECT MODIFIERS */
	
	public void move(int obj, Location fr, Location to)
	{
		switch (obj)
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

		super.move(obj, fr, to);
	}
	
	protected void addAgent(int x, int y, char letter, String color) 
	{
		Agent agent = new Agent(x, y, letter, color);
		int number = agent.getNumber();
		
		add(AGENT, x, y);
		agents[number] = agent;
		agentArray[x][y] = agent;
	}
	
	protected void addGoal(int x, int y, char letter) 
	{
		Goal goal = new Goal(x, y, letter);
		
		add(GOAL, x, y);
		goals.add(goal);
		goalArray[x][y] = goal;
		addToMap(goalMap, letter, goal);
	}
	
	protected void addBox(int x, int y, char upperCaseLetter, String color) 
	{
		char letter = Character.toLowerCase(upperCaseLetter);
		
		Box box = new Box(x, y, letter, color);
		
		add(BOX, x, y);
		boxes.add(box);
		boxArray[x][y] = box;
		addToMap(boxMap, letter, box);
	}
	
	protected void addWall(int x, int y)
	{
		add(WALL, x, y);
	}
	
	private <T> void addToMap(Map<Character, Set<T>> map, char letter, T object)
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
