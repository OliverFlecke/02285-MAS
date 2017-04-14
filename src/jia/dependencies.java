package jia;

import java.util.List;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;
import srch.dep.DepSearch;

public class dependencies extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

		int frX = (int) ((NumberTerm) args[0]).solve();
        int frY = (int) ((NumberTerm) args[1]).solve();
        int toX = (int) ((NumberTerm) args[2]).solve();
        int toY = (int) ((NumberTerm) args[3]).solve();

        Location from = new Location(frX, frY);
        Location to   = new Location(toX, toY);	        
        int object = (int) ((NumberTerm) args[4]).solve();
        
        ListTermImpl terms = new ListTermImpl();
        
        List<Location> locations = DepSearch.search(from, to, object);
        
        for (Location loc : locations)
        {
        	NumberTerm locX = new NumberTermImpl(loc.x);
        	NumberTerm locY = new NumberTermImpl(loc.y);
        	Literal literal = new LiteralImpl("box");
        	literal.addTerms(locX, locY);
        	terms.add(literal);
        }
		
		return un.unifies(args[5], terms); 
	}
}
