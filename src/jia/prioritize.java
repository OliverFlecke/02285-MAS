package jia;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;

public class prioritize extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		
		Map<Location, Set<Location>> dependencies = new HashMap<>();
		
		for (Term t1 : ((ListTerm) args[0]).getAsList())
		{
			Term[] goal = ((Literal) t1).getTermsArray();
			
			int goalX = (int) ((NumberTerm) goal[0]).solve();
			int goalY = (int) ((NumberTerm) goal[1]).solve();
			
			Location goalLoc = new Location(goalX, goalY);
			
			if (!dependencies.containsKey(goalLoc))
			{
				dependencies.put(goalLoc, new HashSet<>());
			}
			
			for (Term t2 : ((ListTerm) goal[2]).getAsList())
			{
				Term[] depend = ((Literal) t2).getTermsArray();
				
				int dependX = (int) ((NumberTerm) depend[0]).solve();
				int dependY = (int) ((NumberTerm) depend[1]).solve();
				
				Location dependLoc = new Location(dependX, dependY);
				
				if (dependencies.containsKey(dependLoc))
				{
					dependencies.get(dependLoc).add(goalLoc);
				}
				else 
				{
					dependencies.put(dependLoc, new HashSet<>(Arrays.asList(goalLoc)));
				}
			}
		}
		
		
		Map<Location, Set<Location>> sorted = dependencies.entrySet().stream()
		        .sorted(Comparator.comparingInt(e->e.getValue().size()))
		        .collect(Collectors.toMap(
		                Map.Entry::getKey,
		                Map.Entry::getValue,
		                (a,b) -> {throw new AssertionError();},
		                LinkedHashMap::new
		        )); 
        
        ListTermImpl prioritizations = new ListTermImpl();
		
        // Not sure if this maintains sorted order
		for (Entry<Location, Set<Location>> entry : sorted.entrySet())
//        for (Location loc : sorted.keySet())
		{
			Location loc = entry.getKey();
        	NumberTerm locX = new NumberTermImpl(loc.x);
        	NumberTerm locY = new NumberTermImpl(loc.y);
        	Literal literal = new LiteralImpl("goal");
        	literal.addTerms(locX, locY);
        	prioritizations.add(literal);
		}	
		
		return un.unifies(args[1], prioritizations);
	}
}
