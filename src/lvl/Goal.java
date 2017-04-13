package lvl;

import env.WorldModel;

public class Goal extends Cell {

	private char character;
	
	public Goal(int x, int y, char character)
	{
		super(x, y);
		
		this.character = Character.toUpperCase(character);
	}

	public char getCharacter() {
		return character;
	}
	
	public boolean isSolved()
	{
		Box box = WorldModel.getInstance().getBoxArray()[this.getLocation().x][this.getLocation().y];
		if (box == null)
			return false;
		else
			return Character.toLowerCase(box.getCharacter()) == Character.toLowerCase(this.getCharacter());
	}
}
