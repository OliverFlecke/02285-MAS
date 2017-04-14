package jia;

import java.util.List;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;
import srch.str.StrSearch;

public class storages extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		

		int frX = (int) ((NumberTerm) args[0]).solve();
        int frY = (int) ((NumberTerm) args[1]).solve();
        
        Location from = new Location(frX, frY);
        
        int object = (int) ((NumberTerm) args[2]).solve();
        
        ListTermImpl storages = new ListTermImpl();
        
        List<Location> locations = StrSearch.search(from, object);
        
        for (Location loc : locations)
        {
        	NumberTerm locX = new NumberTermImpl(loc.x);
        	NumberTerm locY = new NumberTermImpl(loc.y);
        	Literal literal = new LiteralImpl("storage");
        	literal.addTerms(locX, locY);
        	storages.add(literal);
        }		
		return un.unifies(args[3], storages); 
	}
}
