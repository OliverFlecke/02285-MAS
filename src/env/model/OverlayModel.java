package env.model;

import java.util.List;

import level.Location;

public class OverlayModel extends DataModel {
	
	private int overlayObject;

	public OverlayModel(int obj, List<Location> overlay) 
	{
		super(WorldModel.getInstance());
		
		overlayObject = obj;

		for (Location loc : overlay)
		{
			add(overlayObject, loc);
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
				if (hasObject(overlayObject, x, y)) str.append('-');
				else if (hasObject(WALL, x, y)) 	str.append('+');
				else 								str.append(' ');
			}
			str.append("\n");
		}
		return str.toString();
	}
}
