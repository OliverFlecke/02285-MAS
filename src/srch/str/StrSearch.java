package srch.str;

import java.util.ArrayList;
import java.util.List;
import jason.environment.grid.Location;
import srch.Node;
import srch.Search;
import srch.Strategy.BFS;

public class StrSearch extends Search {
	
	/**
	 * 
	 * @param initial
	 * @param object
	 * @return
	 */
	public static List<Location> search(Location initial, int object) {
		return new StrSearch().search(new StrNode(initial, object, new ArrayList<Location>()));
	}
	
	public StrSearch()
	{
		this.setStrategy(new BFS());
	}

	@Override
	public boolean isGoalState(Node n) {
		return false;
	}
}
