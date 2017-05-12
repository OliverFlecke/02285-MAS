package env.model;

import java.util.List;

import level.Location;

public class OverlayModel extends DataModel {
	
	private int overlayObject;
	
	public OverlayModel()
	{
		this(IN_USE);
	}
	
	public OverlayModel(int obj)
	{
		super(WorldModel.getInstance());		

		// Remove everything except walls
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (isFree(WALL, x, y)) data[x][y] = 0;
			}
		}
		
		overlayObject = obj;
	}
	
	public OverlayModel(OverlayModel overlay, List<Location> overlayLocations)
	{
		this(IN_USE, overlay);
		
		for (Location loc : overlayLocations)
		{
			add(overlayObject, loc);
		}
	}

	public OverlayModel(int obj, OverlayModel overlay) 
	{
		this(obj);

		deepAddData(overlay);
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
				if (hasObject(overlayObject, x, y)) str.append('-');
				else if (hasObject(WALL, x, y)) 	str.append('+');
				else 								str.append(' ');
			}
			str.append("\n");
		}
		return str.toString();
	}
}
