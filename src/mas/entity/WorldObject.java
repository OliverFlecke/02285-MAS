package mas.entity;

public abstract class WorldObject {

	private char id;
	
	public char row;
	public char col;
	
	public Color color;
	
	/**
	 * Create an world object with an id, and a position
	 * @param id of the agent
	 * @param row the agent is positioned at
	 * @param col the agent is positioned at
	 */
	protected WorldObject(char id, char row, char col, Color color)
	{
		this.id = id;
		this.row = row;
		this.col = col;
		this.color = color;
	}
	
	/**
	 * Create a world object without a location
	 * @param id of the object
	 * @param color of the object
	 */
	public WorldObject(char id, Color color) {
		if (color == null) color = Color.Blue;
		this.id = id;
		this.color = color;
	}

	/**
	 * @return The id of this object. A number for a agent, and a letter for a box
	 */
	public char getId()
	{
		return this.id;
	}
}
