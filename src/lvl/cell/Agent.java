package lvl.cell;

public class Agent extends Colored {

	private int number;
	
	public Agent(int x, int y, char letter, String color)
	{
		super(x, y, letter, color);
		
		this.number = Character.getNumericValue(letter);
	}
	
	public int getNumber() 
	{
		return number;
	}
}
