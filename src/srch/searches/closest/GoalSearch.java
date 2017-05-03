package srch.searches.closest;

import env.model.DataWorldModel;
import env.model.WorldModel;
import level.Location;
import level.cell.Goal;
import srch.Node;
import srch.nodes.ClosestNode;

public class GoalSearch extends ClosestSearch {

	public static Location search(String color, Location from) 
	{
		return new GoalSearch(color).search(new ClosestNode(from));
	}
	
	private String color;

	public GoalSearch(String color) 
	{
		super(DataWorldModel.GOAL);
		
		this.color = color;
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		if (!super.isGoalState(n))
		{
			return false;
		}
		
		Goal goal = WorldModel.getInstance().getGoal(n.getLocation());		
		
		return !goal.isSolved() && goal.getBox().getColor().equals(color);
	}

}
