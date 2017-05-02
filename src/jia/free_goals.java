package jia;

import java.util.Set;
import java.util.stream.Collectors;

import env.model.WorldModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;
import lvl.cell.Goal;

/**
 * Simple internal action to check in there is any goals left to be solved
 */
public class free_goals extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;

	@Override
	public synchronized Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception
	{
			String color = ((Atom) args[0]).getFunctor();
			Set<Goal> goals = WorldModel.getInstance().getUnsolvedGoals()
					.stream().filter(x -> x.getBox().getColor().equals(color))
					.collect(Collectors.toSet());
			
//			ts.getLogger().info(color + " matches the goals: " + goals.toString());
			
			return goals.size() > 0;
	}
}
