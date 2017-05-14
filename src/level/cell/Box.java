package level.cell;

import env.model.WorldModel;
import level.Color;

public class Box extends Colored 
{
	private static int counter = 0;
	
	private Agent agent;
	private Goal goal;
	private int id; 
	

	public Box(int x, int y, char letter, Color color) {
		super(x, y, letter, color);
		this.id = counter++;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public Goal getGoal() {
		return goal;
	}
	
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	
	/**
	 * @return True if the box is already on a goal
	 */
	public boolean onGoal()
	{		
		Goal goal = WorldModel.getInstance().getGoal(this.getLocation());
		if (goal != null)
			return goal.getLetter() == this.getLetter();
		else
			return false;
	}
	
	@Override
	public String toString() {
		return "Box: " + super.toString();
	}
}
