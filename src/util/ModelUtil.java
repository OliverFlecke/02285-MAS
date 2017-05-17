package util;

import env.model.DataModel;
import level.Location;
import level.cell.Agent;
import level.cell.Box;
import level.cell.Goal;

public class ModelUtil {
	
	private ModelUtil() {}	
	
	public static int getAgentNumber(Agent agent) {
		return ((int) agent.getLetter()) << 24;
	}
	
	public static int getAgentNumber(Location loc, DataModel model)
	{
		if (model.hasObject(DataModel.AGENT, loc))
		{
			return model.getMasked(DataModel.BOX_MASK, loc) >> 24;
		}
		return 0;
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

}
