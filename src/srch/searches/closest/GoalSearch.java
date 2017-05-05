package srch.searches.closest;

import env.model.DataWorldModel;
import level.Location;
import srch.Node;
import srch.nodes.ClosestNode;

public class GoalSearch extends ClosestSearch {

	public static Location search(DataWorldModel model, Location from) 
	{
		return new GoalSearch(model).search(new ClosestNode(from));
	}
	
	private DataWorldModel model;

	public GoalSearch(DataWorldModel model) 
	{
		super(DataWorldModel.GOAL);
		
		this.model = model;
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		if (!super.isGoalState(n))
		{
			return false;
		}
		return model.isSolved(model.getGoal(n.getLocation()));
	}

}
