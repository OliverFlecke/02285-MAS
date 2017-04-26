package srch;

public abstract class Search {
	
	protected Strategy strategy;
	
	public void setStrategy(Strategy s) {
		strategy = s;
	}
	
	public <T> T search(Node initial)
	{
		strategy.addToFrontier(initial);
		
		while (!strategy.frontierIsEmpty())
		{
			Node leaf = strategy.getAndRemoveLeaf();
			
			if (isGoalState(leaf))
			{
				return leaf.extractPlan();
			}
			
			strategy.addToExplored(leaf);
			
			for (Node n : leaf.getExpandedNodes())
			{
				if (!strategy.isExplored(n) && !strategy.inFrontier(n))
				{
					strategy.addToFrontier(n);
				}
			}
		}
		return null;
	}
	
	public abstract boolean isGoalState(Node n);
}
