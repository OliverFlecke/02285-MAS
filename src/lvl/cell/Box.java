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
		Goal goal = WorldModel.getInstance().getGoalsArray()[this.getLocation().x][this.getLocation().y];
		if (goal != null)
			return goal.getLetter() == this.getLetter();
		else
			return false;
	}
}
