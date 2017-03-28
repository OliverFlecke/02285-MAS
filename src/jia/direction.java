package jia;

import java.util.logging.Level;

import env.WorldModel;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;

/**
 * Gets the direction towards some location using A*(?).
 *
 */
public class direction extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception
	{
		try {
			int agX = (int) ((NumberTerm) terms[0]).solve();
	        int agY = (int) ((NumberTerm) terms[1]).solve();
	        int toX = (int) ((NumberTerm) terms[2]).solve();
	        int toY = (int) ((NumberTerm) terms[3]).solve();
	        
	        Location from = new Location(agX, agY);
	        Location to   = new Location(toX, toY);
	        
	        /*
	         * TODO:
	         * A* search using from, to and a reference to the model.
	         * Should the model be local to each agent, or static using the singleton pattern?
	         */
	        
	        return WorldModel.Direction.N;
		} 
		catch (Throwable e) 
		{
			ts.getLogger().log(Level.SEVERE, "direction error: " + e, e);
		}
		return false;
	}
}
