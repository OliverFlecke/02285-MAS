package level.Actions;

public abstract class Action {

	public enum ActionType {
		MOVE,
		PUSH, 
		PULL,
		SKIP
	}
	
	private ActionType type;
	
	protected Action(ActionType type)
	{
		this.type = type;
	}

	public ActionType getType()
	{
		return this.type;
	}
	
//	@Override
//	public String toString()
//	{
//		switch (type)
//		{
//		case MOVE: return "Move(" + toString(action.getTerm(0)) + ")";
//		case PUSH: return "Push(" + toString(action.getTerm(0)) + "," 
//								  + toString(action.getTerm(1)) + ")";
//		case PULL: return "Pull(" + toString(action.getTerm(0)) + "," 
//		  						  + toString(action.getTerm(1)) + ")";
//		default  : return "NoOp";
//		}
//	}
}


