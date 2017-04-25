package srch.clo;

import env.model.WorldModel;
import jason.environment.grid.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BFS;

public class CloSearch extends Search {

	public static Location search(int object, Location from) {
		return new CloSearch(object).search(new CloNode(from));
	}
	
	private int object;
	
	public CloSearch(int object)
	{
		this.setStrategy(new BFS());
		
		this.object = object;
	}

	@Override
	public boolean isGoalState(Node n) {
		return WorldModel.getInstance().hasObject(n.getLocation(), object);
	}
}
