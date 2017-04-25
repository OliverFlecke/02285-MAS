package srch.goal;

import env.model.WorldModel;
import jason.environment.grid.Location;
import srch.Node;
import srch.clo.CloNode;
import srch.clo.CloSearch;

public class GoalSearch extends CloSearch {

	public static Location search(Location from) 
	{
		return new GoalSearch().search(new CloNode(from));
	}

	public GoalSearch() 
	{
		super(WorldModel.GOAL);
	}
	
	@Override
	public boolean isGoalState(Node n) 
	{
		return super.isGoalState(n) && !WorldModel.getInstance().getGoal(n.getLocation()).isSolved();
	}

}
