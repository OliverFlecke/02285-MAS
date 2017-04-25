package jia;

import env.planner.Planner;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import jason.environment.grid.Location;
import lvl.cell.Goal;

public class plan_select_goal extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		
		int agX = (int) ((NumberTerm) terms[0]).solve();
        int agY = (int) ((NumberTerm) terms[1]).solve();
        
        Goal goal = Planner.selectGoal(agX, agY);
        
        if (goal == null)
        	return false;
        
        Location goalLoc = goal.getLocation();
        
        Location boxLoc = goal.getBox().getLocation();
        
        Term boxX  = ASSyntax.createNumber(boxLoc.x );
        Term boxY  = ASSyntax.createNumber(boxLoc.y );
        Term goalX = ASSyntax.createNumber(goalLoc.x);
        Term goalY = ASSyntax.createNumber(goalLoc.y);
        
        
		return  un.unifies(terms[2], boxX ) && 
				un.unifies(terms[3], boxY ) && 
				un.unifies(terms[4], goalX) && 
				un.unifies(terms[5], goalY);
	}

}
