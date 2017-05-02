package level.Actions;

public class SkipAction extends Action {

	public SkipAction()
	{
		super(ActionType.SKIP);
	}
	
	@Override
	public String toString()
	{
		return "NoOp";
	}
}
