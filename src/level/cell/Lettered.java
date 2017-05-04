package level.cell;

import level.Location;

public abstract class Lettered extends Cell {
	
	private char letter;
	
	public Lettered(Location location, char letter)
	{
		super(location);
		
		this.letter = letter;
	}
	
	public Lettered(int x, int y, char letter)
	{
		this(new Location(x, y), letter);
	}
	
	public char getLetter()
	{
		return letter;
	}
	
	@Override
	public String toString() {
		return letter + " " + super.toString();
	}

}
