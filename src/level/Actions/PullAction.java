package level.Actions;

public class PullAction extends Action {

	private Direction agentDir;
	private Diraction boxDir;
	
	public PullAction(Direction agentDir, Direction boxDir)
	{
		super(ActionType.PULL);
		this.agentDir = agentDir;
		this.boxDir = boxDir;
	}
	
	@Override 
	public String toString()
	{
		return "Pull(" + Direction.toString(this.agentDir) + ","
				+ Direction.toString(this.boxDir) + ")";
	}
}
