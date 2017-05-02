package level.cell;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import env.model.WorldModel;

public class Goal extends Lettered {
	
	private Box 		box;
	private Set<Goal> 	dependencies;
	
	public Goal(int x, int y, char letter)
	{
		super(x, y, letter);
		
		dependencies = new HashSet<>();
	}
	
	public boolean isSolved()
	{
		Box box = WorldModel.getInstance().getBox(this.getLocation());
		if (box == null)
			return false;
		else
			return Character.toLowerCase(box.getLetter()) == Character.toLowerCase(this.getLetter());
	}
	
	public Box getBox() {
		return box;
	}
	
	public void setBox(Box box) {
		this.box = box;
	}
	
	public boolean hasDependencies() {
		
		Iterator<Goal> it = dependencies.iterator();
		
		while (it.hasNext())
		{
			Goal goal = it.next();
			
			if (goal.isSolved())
			{
				it.remove();
			}
		}
		
		return dependencies.size() > 0;
	}
	
	public Set<Goal> getDependencies() {
		return dependencies;
	}
	
	public Goal getDependency() {
		return dependencies.iterator().next();
	}
	
	public void addDependency(Goal goal) {
		dependencies.add(goal);
	}
}
