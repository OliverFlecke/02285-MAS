package env.model;

import java.util.Arrays;

import jason.environment.grid.Location;

public class GridWorldModel {
	
    public static final int 	AGENT   = 1;
	public static final int		GOAL 	= 2;
	public static final int		BOX		= 4;
    public static final int 	WALL 	= 8;
	
	protected int				width, height;
    
    private int[][]             data; 
	
	public GridWorldModel(int width, int height)
	{
		this.width 	= width;
		this.height	= height;
		
		this.data = new int[width][height];
	}
    
    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }
    
    public boolean inGrid(int x, int y) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    public boolean hasObject(Location l, int... objs) {
        return hasObject(l.x, l.y, objs);
    }
    
    public boolean hasObject(int x, int y, int... objs) {
        return inGrid(x, y) && Arrays.stream(objs).allMatch(obj -> (data[x][y] & obj) != 0);
    }

    public boolean isFree(Location l) {
        return isFree(l.x, l.y);
    }

    public boolean isFree(int x, int y) {
        return inGrid(x, y) 
        		&& (data[x][y] & WALL) 	== 0 
        		&& (data[x][y] & AGENT) == 0 
        		&& (data[x][y] & BOX) 	== 0;
    }

    public boolean isFree(Location l, int... objs) {
        return isFree(l.x, l.y, objs);   
    }
    
    public boolean isFree(int x, int y, int... objs) {
    	return inGrid(x, y) && Arrays.stream(objs).allMatch(obj -> (data[x][y] & obj) == 0);
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
}
