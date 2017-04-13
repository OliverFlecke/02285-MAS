package lvl;

public class Agent extends Colored {

	private char character;
	private int number;
	
	public Agent(int x, int y, char character, int number, String color)
	{
		super(x, y, color);
		
		this.character = character;
		this.number = number;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public int getNumber() {
		return number;
	}
}
