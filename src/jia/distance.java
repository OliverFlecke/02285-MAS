package jia;

import java.util.logging.Level;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;

/**
 * Gets the manhattan distance between two points.
 * 
 */
public class distance extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception
	{
		try {
			int agX = (int) ((NumberTerm) terms[0]).solve();
	        int agY = (int) ((NumberTerm) terms[1]).solve();
	        int toX = (int) ((NumberTerm) terms[2]).solve();
	        int toY = (int) ((NumberTerm) terms[3]).solve();
	        
	        int dist = new Location(agX, agY).distance(new Location(toX, toY));
	        
	        return un.unifies(terms[4], new NumberTermImpl(dist));  
		} 
		catch (Throwable e) 
		{
			ts.getLogger().log(Level.SEVERE, "distance error: " + e, e);
		}
		return false;
	}
}
