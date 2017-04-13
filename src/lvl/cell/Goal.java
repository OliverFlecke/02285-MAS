package lvl.cell;

import env.WorldModel;

public class Goal extends Lettered {
	
	public Goal(int x, int y, char letter)
	{
		super(x, y, letter);
	}
	
	public boolean isSolved()
	{
		Box box = WorldModel.getInstance().getBoxArray()[this.getLocation().x][this.getLocation().y];
		if (box == null)
			return false;
		else
			return Character.toLowerCase(box.getLetter()) == Character.toLowerCase(this.getLetter());
	}
}
