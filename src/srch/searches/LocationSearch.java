package srch.searches;

import java.util.List;

import jason.environment.grid.Location;
import srch.nodes.LocationNode;

public class LocationSearch {

	public static List<Location> search(Location from, Location to, int proximity) 
	{
		return new DirectionSearch(to, proximity).search(new LocationNode(from));
	}
	
}
