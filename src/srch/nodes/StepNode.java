package srch.nodes;

import level.Location;
import srch.Node;

public abstract class StepNode extends Node {

	private int step;
	
	public StepNode(Location initial, int initialStep) 
	{
		super(initial);
		
		this.step = initialStep;
	}
	
	public StepNode(Location initial, int object, int initialStep)
	{
		super(initial, object);
		
		this.step = initialStep;
	}
	
	public StepNode(StepNode parent, Location location)
	{
		super(parent, location);
		
		this.step = parent.getStep() + 1;
	}
	
	public int getStep()
	{
		return this.step;
	}

}
