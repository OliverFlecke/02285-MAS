package level.cell;

import jason.environment.grid.Location;
import level.Color;

public class Agent extends Colored {

	private int number;
	
	public Agent(Location location, char letter, Color color)
	{
		super(location, letter, color);
		
		this.number = Character.getNumericValue(letter);
	}
	
	public Agent(int x, int y, char letter, Color color)
	{
		this(new Location(x, y), letter, color);
	}
	
	public Agent(Agent agent)
	{
		this(agent.getLocation(), agent.getLetter(), agent.getColor());
	}
	
	public int getNumber() 
	{
		return number;
	}
	
	/**
	 * @return The name of the agent, which is 'agent{id}'
	 */
	public String getName()
	{
		return "agent" + number;
	}
}
