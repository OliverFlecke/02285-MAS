package srch.searches.closest;

import java.util.HashMap;
import java.util.Set;

import env.model.WorldModel;
import level.Location;
import level.cell.Box;
import srch.Node;
import srch.nodes.ClosestNode;

public class BoxSearch extends ClosestSearch {

	public static HashMap<Box, Integer> search(Set<Box> boxes, char letter, Location from) 
	{
		BoxSearch boxSearch = new BoxSearch(letter);
		boxSearch.search(new ClosestNode(from, WorldModel.getInstance()));
		
		HashMap<Box, Integer> distances = boxSearch.getMap();
		for (Box box : boxes)
		{
			if (!distances.containsKey(box))
			{
				distances.put(box, Integer.MAX_VALUE);
			}
		}
		
		return distances;
	}
	
	private char letter;
	
	private HashMap<Box, Integer> distances;

	public BoxSearch(char letter) 
	{
		super(WorldModel.BOX);
		
		this.letter = letter;
		
		distances = new HashMap<Box, Integer>();
	}
	
	private HashMap<Box, Integer> getMap()
	{
		return this.distances;
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		if (!super.isGoalState(n))
		{
			return false;
		}
		
		Box box = WorldModel.getInstance().getBox(n.getLocation());
		
		if (box.getLetter() == letter)
		{
			distances.put(box, n.g());
		}
		return false;
	}

}
