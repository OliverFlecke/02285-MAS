// Internal action code for project 02285-MAS

package jia;

import env.*;

import env.WorldModel;
import jason.asSemantics.*;
import jason.asSyntax.*;
import mas.entity.*;

public class select_box extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
        // execute the internal action
        ts.getAg().getLogger().info("executing internal action 'jia.select_box'");
        
        try 
        {
        	Color color = Color.getColor(((Atom) terms[0]).getFunctor());
        	char letter = ((Atom) terms[1]).getFunctor().charAt(0);
        	int x = (int) ((NumberTerm) terms[2]).solve();
        	int y = (int) ((NumberTerm) terms[3]).solve();
        	Level level = WorldModel.getLevel();
        	
        	// Not using max int, as I need to add them and don't want overflow. 
        	int bestX = 1000;
        	int bestY = 1000;
        	for (Box box : level.getBoxes())
        	{
        		if (box.color == color && Character.toLowerCase(box.getId()) == letter)
        		{
        			if (manhattanDistance(bestX, bestY, x, y) > manhattanDistance(box.col, box.row, x, y))
        			{
        				bestX = box.col;
        				bestY = box.row;
        			}
        		}
        	}
//        	ts.getAg().getLogger().info("Best box at: " + bestX + ":" + bestY);
        	return un.unifies(terms[4], new NumberTermImpl(bestX)) && un.unifies(terms[5], new NumberTermImpl(bestY));
        }
        catch (Throwable e)
        {
        	ts.getLogger().log(java.util.logging.Level.SEVERE, "select box error: " + e, e);
            return false;
        }
    }
    
    private int manhattanDistance(int x0, int y0, int x1, int y1) 
    {
    	return Math.abs(x0 - x1) + Math.abs(y0 - y1);
    }
}
