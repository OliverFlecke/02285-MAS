package srch.searches.closest;

import env.model.CellModel;
import level.Location;
import srch.Node;
import srch.nodes.ClosestNode;

public class GoalSearch extends ClosestSearch {

	public static Location search(CellModel model, Location from) 
	{
		return new GoalSearch(model).search(new ClosestNode(from));
	}
	
	private CellModel model;

	public GoalSearch(CellModel model) 
	{
		super(CellModel.GOAL);
		
		this.model = model;
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		if (!super.isGoalState(n))
		{
			return false;
		}
		return model.isSolved(n.getLocation());
	}

}
