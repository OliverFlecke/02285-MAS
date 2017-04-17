package lvl.cell;

import env.model.WorldModel;

public class Box extends Colored {

	public Box(int x, int y, char letter, String color)
	{
		super(x, y, Character.toLowerCase(letter), color);
	}
	
	/**
	 * @return True if the box is already on a goal
	 */
	public boolean onGoal()
	{		
		Goal goal = WorldModel.getInstance().getGoal(this.getLocation());
		if (goal != null)
			return goal.getLetter() == this.getLetter();
		else
			return false;
	}
}
