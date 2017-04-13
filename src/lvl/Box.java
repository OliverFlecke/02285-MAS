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
		Goal goal = WorldModel.getInstance().getGoalsArray()[this.getLocation().x][this.getLocation().y];
		if (goal != null)
			return goal.getCharacter() == this.getCharacter();
		else
			return false;
	}
}
