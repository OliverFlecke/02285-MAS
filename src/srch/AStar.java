package srch;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import jason.environment.grid.Location;

public class AStar implements Comparator<Node> {
	
	/**
	 * Returns an ordered list of strings corresponding to the sequence of
	 * directions to move to get adjacent to the goal.
	 * @param from - Initial location
	 * @param to - Goal location
	 * @return Ordered list of directions
	 */
	public static LinkedList<String> search(Location from, Location to) {
		return new AStar(from, to).search();
	}
	
	private Location goal;
	
	private PriorityQueue<Node> frontier;
	private HashSet<Node> frontierSet;
	private HashSet<Node> explored;
	
	private AStar(Location from, Location to)
	{
		frontier 	= new PriorityQueue<Node>(11, this);
		frontierSet = new HashSet<Node>();
		explored 	= new HashSet<Node>();

		addToFrontier(new Node(from));
		
		goal = to;		
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
		return n.location.distance(goal) == 1;
	}
	
	private int h(Node n) {
		return n.location.distance(goal);
	}
	
	private int f(Node n) {
		return n.g + h(n);
	}

	@Override
	public int compare(Node n1, Node n2) {
		return f(n1) - f(n2);
	}
}
