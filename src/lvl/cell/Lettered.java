package lvl.cell;

public abstract class Lettered extends Cell {
	
	private char letter;
	
	public Lettered(int x, int y, char letter)
	{
		super(x, y);
		
		this.letter = letter;
	}
	
	public char getLetter()
	{
		return letter;
	}

}
