package util;

import level.cell.Agent;
import level.cell.Box;
import level.cell.Goal;

public class ModelUtil {
	
	private ModelUtil() {}	
	
	public static int getAgentNumber(Agent agent) {
		return ((int) agent.getLetter()) << 24;
	}

//	public static GridWorldModel compareModels(GridWorldModel m1, GridWorldModel m2)
//	{
//		int[][] result = new int[m1.data.length][m1.data[0].length];
//		
//	    for (int x = 0; x < result.length; x++) 
//	    {
//	    	for (int y = 0; y < result[0].length; y++)
//	    	{
//	    		result[x][y] = m1.data[x][y] & m2.data[x][y];
//	    	}
//	    }
//	    return new GridWorldModel(result);
//	}
//
//	public static GridWorldModel diffModels(GridWorldModel m1, GridWorldModel m2)
//	{
//		int[][] result = new int[m1.data.length][m1.data[0].length];
//		
//	    for (int x = 0; x < result.length; x++) 
//	    {
//	    	for (int y = 0; y < result[0].length; y++)
//	    	{
//	    		result[x][y] = m1.data[x][y] ^ m2.data[x][y];
//	    	}
//	    }
//	    return new GridWorldModel(result);
//	}
	
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
