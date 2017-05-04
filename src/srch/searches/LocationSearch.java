package srch.searches;

import java.util.List;

import level.Location;
import srch.nodes.LocationNode;

public class LocationSearch {

	public static List<Location> search(Location from, Location to, int proximity, int initialStep) 
	{
		return new PathfindingSearch(from, to).search(new LocationNode(from, initialStep));
	}
	
}
