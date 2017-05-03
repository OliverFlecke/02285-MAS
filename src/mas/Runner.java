package mas;

import env.WorldEnv;
import env.planner.Planner;
import level.action.Action;

public class Runner {
	
	public static void main(String[] args) {
		
		WorldEnv env = new WorldEnv();
		
		for (Action action : Planner.actions.get(0))
			env.scheduleAction(action, 0);
		
	}
}
