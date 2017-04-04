package mas;

public abstract class WorldObject {

	private char id;
	
	public int row;
	public int col;
	
	public String color;
	
	public Command action;
	
	/**
	 * Create an world object with an id, and a position
	 * @param id of the agent
	 * @param row the agent is positioned at
	 * @param col the agent is positioned at
	 */
	protected WorldObject(char id, int row, int col, String color)
	{
		this.id = id;
		this.row = row;
		this.col = col;
		this.color = color;
	}
	
	public int getId()
	{
		return this.id;
	}
}
