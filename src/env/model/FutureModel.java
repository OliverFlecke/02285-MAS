package env.model;

import java.util.HashMap;
import java.util.Map;

import level.Location;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Cell;

public class FutureModel extends CellModel {
	
	private Map<Cell, Location> originalLocations;

	public FutureModel(CellModel model)
	{
		super(model);
		
		originalLocations = new HashMap<>();
	}
	
	private void addOriginalLocation(Cell cell, Location location)
	{
		for (Cell key : originalLocations.keySet())
		{
			if (key == cell) return;
		}
		originalLocations.put(cell, location);
	}
	
	public Map<Cell, Location> getOriginalLocations()
	{
		return originalLocations;
	}
	
	public void move(int obj, Location fr, Location to)
	{
		if (fr.equals(to)) return;
		
		switch (obj)
		{
		case AGENT: 
			Agent agent = agentArray[fr.x][fr.y];
			
			agent.setLocation(to);
			addOriginalLocation(agent, fr);
				 
			agentArray[to.x][to.y] = agent;
			agentArray[fr.x][fr.y] = null;			
			
			break;
			
		case BOX:	
			Box box = boxArray [fr.x][fr.y];
			
			box.setLocation(to);
			addOriginalLocation(box, fr);
			
			boxArray  [to.x][to.y] = box;
			boxArray  [fr.x][fr.y] = null;
			
			break;
			
		default: 	return;
		}
	}
}
