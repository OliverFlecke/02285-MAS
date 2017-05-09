package srch;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Search {
	
	protected static Logger logger = Logger.getLogger(Search.class.getName());
	
	protected Strategy strategy;
	
	public Search()
	{
		logger.setLevel(Level.OFF);
	}
	
	public void setStrategy(Strategy s) {
		strategy = s;
	}
	
	public <T> T search(Node initial)
	{
		strategy.addToFrontier(initial);
		
		int nodeCount = 0, nodesExpanded = 0;
		while (!strategy.frontierIsEmpty())
		{
			Node leaf = strategy.getAndRemoveLeaf();
			nodeCount++;
			
			if (isGoalState(leaf))
			{
				logger.info("Nodes explored: " + nodeCount + " Nodes expanded: " + nodesExpanded);
				return leaf.extractPlan();
			}
			
			strategy.addToExplored(leaf);
			
			for (Node n : leaf.getExpandedNodes())
			{
				if (!strategy.isExplored(n) && !strategy.inFrontier(n))
				{
					nodesExpanded++;
					strategy.addToFrontier(n);
				}
			}
		}
		logger.warning("No solution found! Nodes explored: " + nodeCount + " Nodes expanded: " + nodesExpanded);
		return null;
	}
	
	public abstract boolean isGoalState(Node n);
}
