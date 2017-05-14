package env.model;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import level.Location;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Goal;
import logging.LoggerFactory;
import util.ModelUtil;

public class CellModel extends ActionModel {

	protected static final Logger logger = LoggerFactory.getLogger(CellModel.class.getName());
	
	protected Agent[]					agents;
	protected Set<Goal>					goals;
	protected Set<Box>					boxes;

	protected Agent[][]					agentArray;
	protected Goal[][]					goalArray;
	protected Box[][]					boxArray;
	
	public CellModel(int width, int height, int nbAgs)
	{
		super(width, height);
		
		agents 		= new Agent[nbAgs];
		goals		= new HashSet<>();
		boxes		= new HashSet<>();
		
		agentArray 	= new Agent[width][height];
		goalArray 	= new Goal [width][height];
		boxArray  	= new Box  [width][height];
	}
	
	public CellModel(CellModel model)
	{
		super(model);
		
		agents 		= model.agents.clone();
		goals		= new HashSet<>(model.goals);
		boxes		= new HashSet<>(model.boxes);
		
		agentArray 	= ModelUtil.deepCopyAgents(model.agentArray);
		goalArray 	= ModelUtil.deepCopyGoals(model.goalArray);
		boxArray 	= ModelUtil.deepCopyBoxes(model.boxArray);		
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
	
	public boolean isSolved(Goal goal) {
		return goal.getBox() == null ? false : goal.getLocation().equals(goal.getBox().getLocation());
	}
	
	public boolean isSolved(Box box) {
		return box.getGoal() == null ? false : box.getLocation().equals(box.getGoal().getLocation());
	}
	
	public void move(int obj, Location fr, Location to)
	{
		if (fr.equals(to)) return;
		
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
}
