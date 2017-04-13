package lvl.cell;

import env.WorldModel;

public class Box extends Colored {

	public Box(int x, int y, char letter, String color)
	{
		super(x, y, letter, color);
	}
	
	/**
	 * @return True if the box is already on a goal
	 */
	public boolean onGoal()
	{
		for (Goal goal : WorldModel.getInstance().getGoals())
			if (goal.getLocation() == this.getLocation())
				return true;
		return false;
	}
}
