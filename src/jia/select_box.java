// Internal action code for project 02285-MAS

package jia;

import env.*;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;
import lvl.Color;
import lvl.cell.Box;

public class select_box extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
        // execute the internal action
//        ts.getAg().getLogger().info("executing internal action 'jia.select_box'");
        
        try 
        {
        	Color color = Color.getColor(((Atom) terms[0]).getFunctor());
        	char letter = ((Atom) terms[1]).getFunctor().charAt(0);
        	int goalX = (int) ((NumberTerm) terms[2]).solve();
        	int goalY = (int) ((NumberTerm) terms[3]).solve();
        	
        	// Not using max int, as I need to add them and don't want overflow. 
        	Location best = new Location(1000, 1000);
        	Location goal = new Location(goalX, goalY);
        	int bestDistance = Integer.MAX_VALUE;
        	for (Box box : WorldModel.getInstance().getBoxesNotOnGoal())
        	{
//        		ts.getAg().getLogger().info("Look at box: " + box.getLocation().x + ", " + box.getLocation().y);
//        		ts.getAg().getLogger().info("Color: " + box.getColor() + " == " + color);
//        		ts.getAg().getLogger().info("Letter: " + Character.toLowerCase(box.getLetter()) + " == " + letter);
        		if (box.getColor() == color && Character.toLowerCase(box.getLetter()) == letter)
        		{
        			int distance = box.getLocation().distanceManhattan(goal);
        			if (bestDistance > distance)
        			{
        				best = box.getLocation();
        				bestDistance = distance;
        			}
        		}
        	}
//        	ts.getAg().getLogger().info("Best box at: " + bestX + ":" + bestY);
        	return un.unifies(terms[4], new NumberTermImpl(best.x)) && un.unifies(terms[5], new NumberTermImpl(best.y));
        }
        catch (Throwable e)
        {
        	ts.getLogger().log(java.util.logging.Level.SEVERE, "select box error: " + e, e);
            return false;
        }
    }
}
