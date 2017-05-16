package level.cell;

import java.util.LinkedList;

import level.Color;
import level.Location;

public class Agent extends Colored {

	private int number;
	private LinkedList<Goal> goals;
	
	public Agent(Location location, char letter, Color color, LinkedList<Goal> goals)
	{
		super(location, letter, color);
		
		this.number = Character.getNumericValue(letter);
		this.goals = goals;
	}
	
	public Agent(Location location, char letter, Color color)
	{
		this(location, letter, color, new LinkedList<Goal>());
	}
	
	public Agent(int x, int y, char letter, Color color)
	{
		this(new Location(x, y), letter, color);
	}
	
	public Agent(Agent agent)
	{
		this(new Location(agent.getLocation()), agent.getLetter(), agent.getColor(), agent.goals);
	}
	
	public int getNumber() 
	{
		return number;
	}
	
	public void addGoal(Goal goal) 
	{
		goals.add(goal);
	}
	
	public void removeGoal(Goal goal)
	{
		goals.remove(goal);
	}
	
	public Goal peekFirst()
	{
		return goals.peekFirst();
	}
	
	/**
	 * @return The name of the agent, which is 'agent{id}'
	 */
	public String getName()
	{
		return "agent" + number;
	}
	
	@Override
	public String toString() {
		return "Agent: " + super.toString();
	}
}
