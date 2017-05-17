package level.action;

import level.Location;

public class SkipAction extends Action {

	public SkipAction(Location location)
	{
		super(ActionType.SKIP, location, location);
	}
	
	@Override
	public String toString()
	{
		return "NoOp";
	}
	
	@Override
	public Action getOpposite() {
		return null;
	}
	
	@Override
	public boolean isOpposite(Action action) {
		return false;
	}
}
