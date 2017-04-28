package jia;

import java.util.logging.Level;

import env.planner.Planner;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class solve_dependencies extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception 
	{	
		try 
		{
			int agX  = (int) ((NumberTerm) terms[0]).solve();
	        int agY  = (int) ((NumberTerm) terms[1]).solve();		
			int boxX = (int) ((NumberTerm) terms[2]).solve();
	        int boxY = (int) ((NumberTerm) terms[3]).solve();
			
	        Planner.solveDependencies(agX, agY, boxX, boxY);
			
			return true;
		} 
		catch (Throwable e) 
		{
			ts.getLogger().log(Level.SEVERE, "solve_dependencies error: " + e, e);
			return false;
		}
	}

}