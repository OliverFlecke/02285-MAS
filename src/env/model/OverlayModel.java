package env.model;

import java.util.Collection;

import level.Location;

public class OverlayModel extends DataModel {
	
	private int overlayObject;
	private Collection<Location> agentToBoxPath;
	
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
	
	public OverlayModel(OverlayModel overlay)
	{
		this(IN_USE, overlay);
	}

	public OverlayModel(int obj, OverlayModel overlay) 
	{
		this(obj);

		deepAddData(overlay);
	}
	
	public void addOverlay(Collection<Location> path)
	{
		path.stream().forEach(l -> add(overlayObject, l));
		
		if (agentToBoxPath != null) path.stream().forEach(l -> agentToBoxPath.remove(l));
	}
	
	private void removeOverlay(Collection<Location> path)
	{
		if (path != null) path.stream().forEach(l -> remove(overlayObject, l));
	}
	
	public void addAgentToBoxOverlay(Collection<Location> path)
	{		
		addOverlay(path);
		agentToBoxPath = path;
	}
	
	public void removeAgentToBoxOverlay()
	{
		removeOverlay(agentToBoxPath);
		agentToBoxPath = null;
	}

	@Override
	public boolean isFree(int x, int y) {
		return super.isFree(overlayObject, x, y);
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
				if (hasObject(overlayObject, x, y)) str.append('#');
				else if (hasObject(WALL, x, y)) 	str.append('+');
				else 								str.append(' ');
			}
			str.append("\n");
		}
		return str.toString();
	}
}
