package mas.entity;

public class Agent extends WorldObject {

	public Agent(char id, char row, char col, Color color) {
		super(id, row, col, color);
	}

	/**
	 * Create an agent with an id and a color, but without a location
	 * @param id of the agent
	 * @param color of the agent
	 */
	public Agent(char id, Color color) {
		super(id, color);
	}
}
