package srch.interfaces;

import env.model.DataModel;
import level.Direction;
import level.action.Action;
import srch.Node;

public class Getter {

	public static Action getAction(Node n) {
		return ((IActionNode) n).getAction();
	}
	
	public static Direction getDirection(Node n) {
		return ((IDirectionNode) n).getDirection();
	}
	
	public static DataModel getModel(Node n) {
		return ((IModelNode) n).getModel();
	}
	
	public static int getDependencyCount(Node n) {
		return ((IDependencyNode) n).getDependencyCount();
	}
}
