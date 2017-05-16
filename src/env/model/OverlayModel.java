package env.model;

import java.util.Collection;
import java.util.Stack;

import level.Location;

public class OverlayModel extends DataModel {
	
	private static final int WALL = 1;
	
	private int overlayObject = 1;
	
	private Stack<Collection<Location>> overlayStack;
	
	public OverlayModel()
	{
		super(WorldModel.getInstance());		

		// Remove everything except walls
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (data[x][y] == DataModel.WALL) data[x][y] = WALL;
				else							  data[x][y] = 0;
			}
		}		
		overlayStack = new Stack<>();
	}
	
	public OverlayModel(OverlayModel overlay)
	{
		this();
		
		deepAddData(overlay);
		
		for (Collection<Location> path : overlay.overlayStack)
		{
			addOverlay(path);
		}
	}
	
	public void addOverlay(Collection<Location> path)
	{		
		overlayObject <<= 1;
		
		path.stream().forEach(l -> add(overlayObject, l));
		
		overlayStack.push(path);
	}
	
	public void removeOverlay()
	{
		if (!overlayStack.isEmpty())
		{			
			overlayStack.pop().stream().forEach(l -> remove(overlayObject, l));
			
			overlayObject >>= 1;
		}
	}

	@Override
	public boolean isFree(int x, int y) {
		return data[x][y] == 0;
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
					 if (data[x][y] == 0)	str.append(' ');
				else if (data[x][y] == 1) 	str.append('+');
				else						str.append('#');
			}
			str.append("\n");
		}
		return str.toString();
	}
}
