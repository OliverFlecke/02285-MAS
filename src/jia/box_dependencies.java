package jia;

import java.util.List;

import env.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.LiteralImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.environment.grid.Location;
import srch.dep.DepSearch;

public class box_dependencies extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

		int agX  = (int) ((NumberTerm) args[0]).solve();
        int agY  = (int) ((NumberTerm) args[1]).solve();
        int boxX = (int) ((NumberTerm) args[2]).solve();
        int boxY = (int) ((NumberTerm) args[3]).solve();
        
        Location agLoc  = new Location(agX , agY );
        Location boxLoc = new Location(boxX, boxY);
        
        ListTermImpl terms = new ListTermImpl();
        
        List<Location> locations = DepSearch.search(boxLoc, agLoc, WorldModel.BOX);
        
        for (Location loc : locations)
        {
        	NumberTerm locX = new NumberTermImpl(loc.x);
        	NumberTerm locY = new NumberTermImpl(loc.y);
        	Literal literal = new LiteralImpl("box");
        	literal.addTerms(locX, locY);
        	terms.add(literal);
        }
		
		return un.unifies(args[4], terms); 
	}
}
