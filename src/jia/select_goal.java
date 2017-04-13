// Internal action code for project 02285-MAS

package jia;

import env.WorldModel;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;
import lvl.Goal;

public class select_goal extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
        // execute the internal action
//        ts.getAg().getLogger().info("executing internal action 'jia.select_goal'");
        
        try 
        {
        	int agentX = (int) ((NumberTerm) terms[1]).solve();
        	int agentY = (int) ((NumberTerm) terms[2]).solve();
        	
        	Goal bestGoal = null;
        	Location best = new Location(1000, 1000);
        	Location agent = new Location(agentX, agentY);
        	int bestDistance = Integer.MAX_VALUE;
        	
        	for (Goal goal : WorldModel.getInstance().getUnsolvedGoals())
        	{
        		int distance = goal.getLocation().distanceManhattan(agent);
        		if (bestDistance > distance)
        		{
        			best = goal.getLocation();
        			bestDistance = distance;
        			bestGoal = goal;
        		}
        	}
        	if (bestGoal == null) return false;
        	
        	return un.unifies(terms[0], new LiteralImpl(Character.toString(Character.toLowerCase(bestGoal.getCharacter())))) && 
        			un.unifies(terms[3], new NumberTermImpl(best.x)) && 
        			un.unifies(terms[4], new NumberTermImpl(best.y));
        }
        catch (Throwable e)
        {
        	ts.getLogger().log(java.util.logging.Level.SEVERE, "select box error: " + e, e);
            return false;
        }
    }
}
