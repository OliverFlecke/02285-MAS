package srch;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import jason.environment.grid.Location;

public class AStar implements Comparator<Node> {
	
	/**
	 * Returns an ordered list of strings corresponding to the sequence of
	 * directions to move to get to the goal, depending on the proximity.
	 * The A* considers all boxes as obstacles, even if the box is on the 
	 * goal location. 
	 * @param from - Initial location.
	 * @param to - Goal location.
	 * @param proximity - The distance between the goal and the solution. 
	 * With proximity = 0, the solution is a path to the goal location.
	 * With proximity = 1, the solution is a path to a cell adjacent to the goal location.
	 * @return Ordered list of directions leading to the goal.
	 */
	public static LinkedList<String> search(Location from, Location to, int proximity) {
		return new AStar(from, to, proximity).search();
	}
	
	private Location goalLocation;
	private int 	 goalDistance;
	
	private PriorityQueue<Node> frontier;
	private HashSet<Node> frontierSet;
	private HashSet<Node> explored;
	
	private AStar(Location from, Location to, int proximity)
	{
		frontier 	= new PriorityQueue<Node>(11, this);
		frontierSet = new HashSet<Node>();
		explored 	= new HashSet<Node>();

		addToFrontier(new Node(from));
		
		goalLocation = to;
		goalDistance = proximity;
	}
	
	private LinkedList<String> search()
	{		
		while (!frontier.isEmpty())
		{
			Node leaf = getAndRemoveLeaf();
			
			if (isAdjacentToGoal(leaf))
			{
				return leaf.extractPlan();
			}
			
			addToExplored(leaf);
			
			for (Node n : leaf.GetExpandedNodes())
			{
				if (!isExplored(n) && !inFrontier(n))
				{
					addToFrontier(n);
				}
			}
		}		
		return null;
	}
	
	private Node getAndRemoveLeaf() {
		Node n = frontier.poll();
		frontierSet.remove(n);
		return n;
	}

	private void addToFrontier(Node n) {
		frontier.add(n);
		frontierSet.add(n);
	}
	
	private boolean inFrontier(Node n) {
		return frontierSet.contains(n);
	}

	private void addToExplored(Node n) {
		explored.add(n);
	}

	private boolean isExplored(Node n) {
		return explored.contains(n);
	}
	
	private boolean isAdjacentToGoal(Node n) {
		return n.location.distance(goalLocation) == goalDistance;
	}
	
	private int h(Node n) {
		return n.location.distance(goalLocation);
	}
	
	private int f(Node n) {
		return n.g + h(n);
	}

	@Override
	public int compare(Node n1, Node n2) {
		return f(n1) - f(n2);
	}
}
