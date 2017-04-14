package jia;

import java.util.List;

import env.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import jason.environment.grid.Location;
import srch.dep.DepSearch;

public class goal_dependencies extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

		int agX   = (int) ((NumberTerm) args[0]).solve();
        int agY   = (int) ((NumberTerm) args[1]).solve();
        int goalX = (int) ((NumberTerm) args[2]).solve();
        int goalY = (int) ((NumberTerm) args[3]).solve();
        
        Location agLoc   = new Location(agX  , agY  );
        Location goalLoc = new Location(goalX, goalY);
        
        ListTermImpl directions = new ListTermImpl();
        
        List<Location> goalLocations = DepSearch.search(goalLoc, agLoc, WorldModel.GOAL);		
		
		return un.unifies(args[5], directions); 
	}
}
