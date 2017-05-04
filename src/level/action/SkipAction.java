package level.action;

import level.Location;

public class SkipAction extends Action {

	public SkipAction(Location location)
	{
		super(ActionType.SKIP, location);
	}
	
	@Override
	public String toString()
	{
		return "NoOp";
	}
}