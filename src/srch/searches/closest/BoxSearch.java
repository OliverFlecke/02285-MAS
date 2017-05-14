package srch.searches.closest;

import java.util.Set;

import env.model.CellModel;
import env.model.WorldModel;
import level.Location;
import level.cell.Box;
import srch.Node;
import srch.nodes.ClosestNode;

public class BoxSearch extends ClosestSearch {

	public static Location search(Set<Box> boxes, char letter, Location from, CellModel model) 
	{
		return new BoxSearch(boxes, letter, model).search(new ClosestNode(from, model));
	}
	
	private Set<Box> boxes;
	private char letter;
	private CellModel model;

	public BoxSearch(Set<Box> boxes, char letter, CellModel model) 
	{
		super(WorldModel.BOX);
		
		this.boxes 	= boxes;
		this.letter = letter;
		this.model  = model;
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		if (!super.isGoalState(n))
		{
			return false;
		}
		
		Box box = model.getBox(n.getLocation());
		
		return box.getLetter() == letter && boxes.contains(box);
	}

}
