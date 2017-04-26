package env.model;

import jason.environment.grid.Location;

public class GridWorldModel {
	
    public static final int 	AGENT   = 1;
	public static final int		GOAL 	= 2;
	public static final int		BOX		= 4;
    public static final int 	WALL 	= 8;
    public static final int 	LOCKED  = 16;
	
	protected int				width, height;
    
    private int[][]             data; 
	
	public GridWorldModel(int width, int height)
	{
		this.width 	= width;
		this.height	= height;
		
		this.data 	= new int[width][height];
	}
	
	public GridWorldModel(int[][] data)
	{
		this.width 	= data.length;
		this.height = data[0].length;
		
		this.data 	= data;
	}
    
    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }
    
    public int[][] getDate()
    {
    	return this.data;
    }
    
    public void lock(Location location)
    {
    	add(LOCKED, location);
    }
    
    public void unlock(Location location)
    {
    	remove(LOCKED, location);
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
        return inGrid(x, y) && (data[x][y] & (WALL | AGENT | BOX)) == 0;
    }

    public boolean isFree(int obj, Location l) {
        return isFree(obj, l.x, l.y);   
    }
    
    public boolean isFree(int obj, int x, int y) {
    	return inGrid(x, y) && (data[x][y] & obj) == 0;
    }
    
    protected void add(int obj, Location l) {
        add(obj, l.x, l.y);
    }

    protected void add(int obj, int x, int y) {
        data[x][y] |= obj;
    }

    protected void remove(int obj, Location l) {
        remove(obj, l.x, l.y);
    }

    protected void remove(int obj, int x, int y) {
        data[x][y] &= ~obj;
    }
	
	public void printLevel()
	{
		// Print integer representation of level
		for (int y = 0; y < height; y++) 
		{
			for (int x = 0; x < width; x++) 
			{
				System.err.print(data[x][y]);
			}
			System.err.println();
		}
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
}
