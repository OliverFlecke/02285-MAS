package mas;

public class Agent {

	private char id;
	
	public int row;
	public int col;
	
	public Command action;
	
	/**
	 * Create an agent with an id, and a position
	 * @param id of the agent
	 * @param row the agent is positioned at
	 * @param col the agent is positioned at
	 */
	public Agent(char id, int row, int col)
	{
		this.id = id;
		this.row = row;
		this.col = col;
	}
	
	public int getId()
	{
		return this.id;
	}
}
