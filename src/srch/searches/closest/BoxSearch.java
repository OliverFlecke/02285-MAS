package srch.searches.closest;

import java.util.Set;

import env.model.WorldModel;
import level.Location;
import level.cell.Box;
import srch.Node;
import srch.nodes.ClosestNode;

public class BoxSearch extends ClosestSearch {

	public static Location search(Set<Box> boxes, char letter, Location from) 
	{
		return new BoxSearch(boxes, letter).search(new ClosestNode(from, WorldModel.getInstance()));
	}
	
	private Set<Box> boxes;
	private char letter;

	public BoxSearch(Set<Box> boxes, char letter) 
	{
		super(WorldModel.BOX);
		
		this.boxes 	= boxes;
		this.letter = letter;
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		if (!super.isGoalState(n))
		{
			return false;
		}
		
		Box box = WorldModel.getInstance().getBox(n.getLocation());
		
		return box.getLetter() == letter && boxes.contains(box);
	}

}
