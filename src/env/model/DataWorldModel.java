package env.model;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import level.Location;
import level.action.MoveAction;
import level.action.PullAction;
import level.action.PushAction;
import level.Direction;
import level.cell.*;
import logging.LoggerFactory;

public class DataWorldModel extends GridWorldModel {

	protected static final Logger logger = LoggerFactory.getLogger(DataWorldModel.class.getName());
	
	protected Agent[]					agents;
	protected Set<Goal>					goals;
	protected Set<Box>					boxes;

	protected Agent[][]					agentArray;
	protected Goal[][]					goalArray;
	protected Box[][]					boxArray;
	
	public DataWorldModel(int width, int height, int nbAgs)
	{
		super(width, height);
		
		agents 		= new Agent[nbAgs];
		goals		= new HashSet<>();
		boxes		= new HashSet<>();
		
		agentArray 	= new Agent[width][height];
		goalArray 	= new Goal [width][height];
		boxArray  	= new Box  [width][height];
	}
	
	public DataWorldModel(DataWorldModel model)
	{
		super(model);
		
		agents 		= model.agents.clone();
		goals		= new HashSet<>(model.goals);
		boxes		= new HashSet<>(model.boxes);
		
		agentArray 	= deepCopyAgents(model.agentArray);
		goalArray 	= deepCopyGoals(model.goalArray);
		boxArray 	= deepCopyBoxes(model.boxArray);		
	}
	
	/**
	 * @param goal
	 * @return True if the passed goal is solved
	 */
	public boolean isSolved(Goal goal)
	{
		Box box = this.getBox(goal.getLocation());
		if (box == null)
			return false;
		else
			return Character.toLowerCase(box.getLetter()) == Character.toLowerCase(goal.getLetter());
	}
//	/**
//	 * @return All the boxes in the world, which is not already on a goal
//	 */
//	public Set<Box> getBoxesNotOnGoal()
//	{
//		return boxes.stream().filter(box -> !box.onGoal()).collect(Collectors.toSet());
//	}
	
	/**
	 * @return Get all the goals that has not been solved yet
	 */
	public Set<Goal> getUnsolvedGoals()
	{
		return goals.stream().filter(goal -> !isSolved(goal)).collect(Collectors.toSet());
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
	
	public Agent[] getAgents() {
		return agents;
	}
	
	public Set<Goal> getGoals() {
		return goals;
	}
	
	public Set<Box> getBoxes() {
		return boxes;
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
	
	public Agent getAgent(int x, int y) {
		return agentArray[x][y];
	}
	
	public Goal getGoal(int x, int y) {
		return goalArray[x][y];
	}
	
	public Box getBox(int x, int y) {
		return boxArray[x][y];
	}
	
	public static Agent[][] deepCopyAgents(Agent[][] data)
	{
		Agent[][] result = new Agent[data.length][];
	    
	    for (int row = 0; row < data.length; row++) 
	    {
	        result[row] = data[row].clone();
	    }
	    return result;
	}
	
	public static Goal[][] deepCopyGoals(Goal[][] data)
	{
		Goal[][] result = new Goal[data.length][];
	    
	    for (int row = 0; row < data.length; row++) 
	    {
	        result[row] = data[row].clone();
	    }
	    return result;
	}
	
	public static Box[][] deepCopyBoxes(Box[][] data)
	{
		Box[][] result = new Box[data.length][];
	    
	    for (int row = 0; row < data.length; row++) 
	    {
	        result[row] = data[row].clone();
	    }
	    return result;
	}
	
	
	/* OBJECT MODIFIERS */
	
	public void move(int obj, Location fr, Location to)
	{
		switch (obj)
		{
		case AGENT: 
//			logger.info(fr.x + "," + fr.y + " has " + agentArray[fr.x][fr.y]);
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

	public void doMove(MoveAction action, int agId) {
		Direction 	dir 	= action.getDirection();
		
	    Location 	agLoc 	= getAgPos(agId);
	    Location 	nAgLoc 	= agLoc.newLocation(dir);
		
	    move(AGENT, agLoc, nAgLoc);
	}

	public void doPush(PushAction action, int agId) {
		Direction 	dir1 	= action.getAgentDir();
		Direction 	dir2 	= action.getBoxDir();
	
	    Location 	agLoc 	= getAgPos(agId);
		Location 	nAgLoc 	= agLoc.newLocation(dir1);
		Location 	nBoxLoc = nAgLoc.newLocation(dir2); 
	
	    move(AGENT, agLoc, nAgLoc);
	    move(BOX, nAgLoc, nBoxLoc);
	}

	public void doPull(PullAction action, int agId) {
		Direction dir1 = action.getAgentDir();
		Direction dir2 = action.getBoxDir();
		
	    Location agLoc 	= getAgPos(agId);        
		Location boxLoc = agLoc.newLocation(dir2);
		Location nAgLoc = agLoc.newLocation(dir1); 
	
		move(AGENT, agLoc, nAgLoc);
		move(BOX, boxLoc, agLoc);
	}
}
