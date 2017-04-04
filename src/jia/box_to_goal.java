package jia;

import java.util.logging.Level;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class box_to_goal extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception 
	{

		try {
//			int agX = (int) ((NumberTerm) args[0]).solve();
//	        int agY = (int) ((NumberTerm) args[1]).solve();
//	        int toX = (int) ((NumberTerm) args[2]).solve();
//	        int toY = (int) ((NumberTerm) args[3]).solve();
	        
//	        Location from = new Location(agX, agY);
//	        Location to   = new Location(toX, toY);
	        
	        Term dir1, dir2;
	        
			dir1 = Literal.parseLiteral("right");
			dir2 = Literal.parseLiteral("right");
	        
	        return un.unifies(args[4], dir1) && un.unifies(args[5], dir2); 
		} 
		catch (Throwable e) 
		{
			ts.getLogger().log(Level.SEVERE, "direction error: " + e, e);
		}
		return false;
	}
}
