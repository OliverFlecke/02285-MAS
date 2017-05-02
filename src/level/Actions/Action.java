package level.Actions;

import java.util.LinkedList;

import level.Direction;

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
	
	public static final Action[] EVERY;
	static {
		LinkedList<Action> actions = new LinkedList<Action>();
		for (Direction d1 : Direction.EVERY) 
			for (Direction d2 : Direction.EVERY) 
				if (!Direction.isOpposite(d1, d2)) 
					actions.add(new PushAction(d1, d2));
		
		for (Direction d1 : Direction.EVERY) 
			for (Direction d2 : Direction.EVERY)
				if (d1 != d2) 
					actions.add(new PullAction(d1, d2));
		
		for (Direction dir : Direction.EVERY) 
			actions.add(new MoveAction(dir));
		
		actions.add(new SkipAction());

		EVERY = (Action[]) actions.toArray();
	}
}


