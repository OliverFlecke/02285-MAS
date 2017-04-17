package lvl.cell;

import env.model.WorldModel;

public class Goal extends Lettered {
	
	public Goal(int x, int y, char letter)
	{
		super(x, y, letter);
	}
	
	public boolean isSolved()
	{
		Box box = WorldModel.getInstance().getBox(this.getLocation());
		if (box == null)
			return false;
		else
			return Character.toLowerCase(box.getLetter()) == Character.toLowerCase(this.getLetter());
	}
}
