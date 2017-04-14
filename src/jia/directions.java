package jia;

import java.util.List;
import java.util.logging.Level;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;
import srch.dir.DirSearch;

/**
 * Gets a sequence of directions towards some location using A*.
 */
public class directions extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception
	{
		try {
			int agX = (int) ((NumberTerm) args[0]).solve();
	        int agY = (int) ((NumberTerm) args[1]).solve();
	        int toX = (int) ((NumberTerm) args[2]).solve();
	        int toY = (int) ((NumberTerm) args[3]).solve();
	        
	        Location from = new Location(agX, agY);
	        Location to   = new Location(toX, toY);	        
	        int proximity = (int) ((NumberTerm) args[4]).solve();
	        
	        ListTermImpl directions = new ListTermImpl();
	        
	        List<String> path = DirSearch.search(from, to, proximity);
	        
	        if (path == null)
	        {
	        	ts.getLogger().warning("Unable to find path from (" + agX + "," + agY + ") to (" + toX + "," + toY + ")");
	        	return false;
	        }
	        
	        for (String dir : path)
	        {
	        	directions.add(Literal.parseLiteral(dir));
	        }	        
	        return un.unifies(args[5], directions); 
		} 
		catch (Throwable e) 
		{
			ts.getLogger().log(Level.SEVERE, "direction error: " + e, e);
		}
		return false;
	}
}
