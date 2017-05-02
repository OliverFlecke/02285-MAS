package srch.nodes;

import jason.environment.grid.Location;
import level.Direction;
import srch.Node;

public abstract class StepNode extends Node {

	private int step;
	
	public StepNode(Location initial, int initialStep) {
		super(initial);
		
		this.step = initialStep;
	}
	
	public StepNode(Location initial, int object, int initialStep)
	{
		super(initial, object);
		this.step = initialStep;
	}
	
	public StepNode(StepNode parent, Direction direction, Location location)
	{
		super(parent, direction, location);
		this.step = parent.getStep() + 1;
	}
	
	public int getStep()
	{
		return this.step;
	}

}
