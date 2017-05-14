package level.cell;

import env.model.CellModel;

public class Goal extends Lettered {
	
	private static int counter = 0;
	
	private Box box;
	private int id;
	
	public Goal(int x, int y, char letter)
	{
		super(x, y, letter);
		this.id = counter++;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public boolean isSolved(CellModel model)
	{
		Box box = model.getBox(this.getLocation());
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
	
	@Override
	public String toString() {
		return "Goal: " + super.toString();
	}
	
//	public boolean hasDependencies(DataWorldModel model) {
//		
//		Iterator<Goal> it = dependencies.iterator();
//		
//		while (it.hasNext())
//		{
//			Goal goal = it.next();
//			
//			if (goal.isSolved(model))
//			{
//				it.remove();
//			}
//		}
//		
//		return dependencies.size() > 0;
//	}
//	
//	public Set<Goal> getDependencies() {
//		return dependencies;
//	}
//	
//	public Goal getDependency() {
//		return dependencies.iterator().next();
//	}
//	
//	public void addDependency(Goal goal) {
//		dependencies.add(goal);
//	}
}
