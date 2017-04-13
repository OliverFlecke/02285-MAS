package lvl;

import env.WorldModel;

public class Box extends Colored {

	private char character;

	public Box(int x, int y, char character, String color)
	{
		super(x, y, color);
		
		this.character = character;
	}

	public char getCharacter() {
		return character;
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
