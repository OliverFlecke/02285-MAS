package level.cell;

import env.model.WorldModel;
import level.Color;

public class Box extends Colored {

	public Box(int x, int y, char letter, Color color)
	{
		super(x, y, letter, color);
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
