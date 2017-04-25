package srch.goal;

import env.model.WorldModel;
import jason.environment.grid.Location;
import lvl.cell.Goal;
import srch.Node;
import srch.clo.CloNode;
import srch.clo.CloSearch;

public class GoalSearch extends CloSearch {

	public static Location search(String color, Location from) 
	{
		return new GoalSearch(color).search(new CloNode(from));
	}
	
	private String color;

	public GoalSearch(String color) 
	{
		super(WorldModel.GOAL);
		
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