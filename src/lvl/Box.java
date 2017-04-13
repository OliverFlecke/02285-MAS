package lvl;

public class Box extends Colored {

	private char character;

	public Box(int x, int y, char character)
	{
		super(x, y);
		
		this.character = character;
	}

	public char getCharacter() {
		return character;
	}
}
