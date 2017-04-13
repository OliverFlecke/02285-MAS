package lvl;

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
}
