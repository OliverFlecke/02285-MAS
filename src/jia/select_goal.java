// Internal action code for project 02285-MAS

package jia;

import java.util.stream.Collectors;

import env.model.WorldModel;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;
import lvl.cell.Goal;

public class select_goal extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
        // execute the internal action
//        ts.getAg().getLogger().info("executing internal action 'jia.select_goal'");
        
        try 
        {
        	String color = ((Atom) terms[0]).getFunctor();
        	int agentX = (int) ((NumberTerm) terms[2]).solve();
        	int agentY = (int) ((NumberTerm) terms[3]).solve();
        	
        	Goal bestGoal = null;
        	Location best = new Location(1000, 1000);
        	Location agent = new Location(agentX, agentY);
        	int bestDistance = Integer.MAX_VALUE;
        	
        	for (Goal goal : WorldModel.getInstance().getUnsolvedGoals())
        	{
        		int distance = goal.getLocation().distanceManhattan(agent);
        		
        		// Should also test if the goal is reachable
        		if (bestDistance > distance &&
        				WorldModel.getInstance().getBoxesNotOnGoal().stream()
        				.filter(box -> box.getLetter() == goal.getLetter() && box.getColor() == color)
        				.collect(Collectors.toSet()).size() > 0) 
        		{
        			best = goal.getLocation();
        			bestDistance = distance;
        			bestGoal = goal;
        		}
        	}
        	if (bestGoal == null) return false;
        	
        	return un.unifies(terms[1], new LiteralImpl(Character.toString(Character.toLowerCase(bestGoal.getLetter())))) && 
        			un.unifies(terms[4], new NumberTermImpl(best.x)) && 
        			un.unifies(terms[5], new NumberTermImpl(best.y));
        }
        catch (Throwable e)
        {
        	ts.getLogger().log(java.util.logging.Level.SEVERE, "select box error: " + e, e);
            return false;
        }
    }
}
