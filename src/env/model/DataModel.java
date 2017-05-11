package env.model;

import java.util.Arrays;
import java.util.logging.Logger;

import level.Color;
import level.Location;

public class DataModel {
	
	protected static final Logger logger = Logger.getLogger(WorldModel.class.getName());
	
    public static final int 	AGENT   =  1;
	public static final int		GOAL 	=  2;
	public static final int		BOX		=  4;
    public static final int 	WALL 	=  8;
    public static final int 	LOCKED 	= 16;
    public static final int 	IN_USE	= 32;

    public static final int 	TYPE_MASK	= 0xFF;
    public static final int 	COLOR_MASK	= 0xFF00;
    public static final int		GOAL_MASK 	= 0xFF0000;
    public static final int		BOX_MASK 	= 0xFF000000;
	
	protected int					width, height;
    
    public int[][]             	data;
    
	
	public DataModel(int width, int height)
	{
		this.width 	= width;
		this.height	= height;		
		this.data 	= new int[width][height];
	}
	
	public DataModel(int[][] data) 
	{
		this.width 	= data.length;
		this.height	= data[0].length;		
		this.data 	= data;
	}
	
	public DataModel(DataModel model)
	{		
		this.data 	= model.deepCopyData();		
		this.width 	= data.length;
		this.height = data[0].length;
	}
	
	public boolean isSolved(Location l) {
		return isSolved(l.x, l.y);
	}
	
	public boolean isSolved(int x, int y) {
		return hasObject(GOAL, x, y) && (data[x][y] & GOAL_MASK) == ((data[x][y] & BOX_MASK) >> 8);
	}
    
    public boolean inGrid(int x, int y) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    public boolean hasObject(int obj, Location l) {
        return hasObject(obj, l.x, l.y);
    }
    
    public boolean hasObject(int obj, int x, int y) {
        return inGrid(x, y) && (data[x][y] & obj) != 0;
    }

    public boolean isFree(Location l) {
        return isFree(l.x, l.y);
    }

    public boolean isFree(int x, int y) {
        return inGrid(x, y) && (data[x][y] & (WALL | AGENT | BOX | LOCKED)) == 0;
    }

    public boolean isFree(int obj, Location l) {
        return isFree(obj, l.x, l.y);
    }
    
    public boolean isFree(int obj, int x, int y) {
    	return inGrid(x, y) && (data[x][y] & obj) == 0;
    }
    
    public boolean isFree(int obj, int mask, Location l) {
    	return isFree(obj, mask, l.x, l.y);
    }
    
    public boolean isFree(int obj, int mask, int x, int y) {
    	return inGrid(x, y) && getMasked(mask, x, y) != obj;
    }
    
    public void add(int obj, Location l) {
        add(obj, l.x, l.y);
    }

    public void add(int obj, int x, int y) {
        data[x][y] |= obj;
    }

    public void remove(int obj, Location l) {
        remove(obj, l.x, l.y);
    }

    public void remove(int obj, int x, int y) {
        data[x][y] &= ~obj;
    }
    
    public void addColor(Color color, int x, int y) {
    	add(Color.getValue(color) << 8, x , y);
    }
    
    public Color getColor(Location l) {
    	return getColor(l.x, l.y);
    }
    
    public Color getColor(int x, int y) {
    	if (!hasObject(AGENT, x, y) && !hasObject(BOX, x, y)) return null;
    	return Color.getColor((data[x][y] & COLOR_MASK) >> 8);
    }
    
    public void addLetter(char letter, int obj, int x, int y) 
    {
    	int ch = ((int) letter);
		
			 if ((obj & GOAL)  != 0) 	ch <<= 16;
		else if ((obj & BOX)   != 0 ||
				 (obj & AGENT) != 0) 	ch <<= 24;
		
		add(ch, x, y);
    }
	
    public int getMasked(int mask, Location l)	{
		return getMasked(mask, l.x, l.y);
	}
	
    public int getMasked(int mask, int x, int y) {
		return data[x][y] & mask;
	}
	
	public int[][] deepCopyData() 
	{
	    int[][] result = new int[data.length][];
	    
	    for (int row = 0; row < data.length; row++) 
	    {
	        result[row] = data[row].clone();
	    }
	    return result;
	}
	
	public void deepAddData(DataModel model)
	{	    
		int[][] data = model.data;
		
	    for (int x = 0; x < data.length; x++) 
	    {
	    	for (int y = 0; y < data[0].length; y++)
	    	{
	    		this.data[x][y] |= data[x][y];
	    	}
	    }
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		// Print integer representation of level
		for (int y = 0; y < height; y++) 
		{
			for (int x = 0; x < width; x++) 
			{
					 if (hasObject(AGENT, x, y)) str.append((char) ((data[x][y] & BOX_MASK) >> 24));
				else if (hasObject(BOX	, x, y)) str.append(Character.toUpperCase((char) ((data[x][y] & BOX_MASK) >> 24)));
				else if (hasObject(GOAL	, x, y)) str.append((char) ((data[x][y] & GOAL_MASK) >> 16));
				else if (hasObject(WALL	, x, y)) str.append('+');
				else str.append(' ');
			}
			str.append("\n");
		}
		return str.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataModel other = (DataModel) obj;
		if (!Arrays.deepEquals(data, other.data))
			return false;
		return true;
	}
}
