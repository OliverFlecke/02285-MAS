package mas;

import env.ServerEnv;
import env.WorldEnv;

public class Runner {
	
	public static void main(String[] args) 
	{	
		long startTime = System.nanoTime();
		
		WorldEnv env = new WorldEnv();
		
		long endTime = System.nanoTime();
		
		double duration = (endTime - startTime) / 1000000000.0;
		
		System.err.println("----------------------------------------------------------------------------------");
		System.err.println("Planning time: " + duration + " \tLength of solution: " + env.planner.getLastStep());
		System.err.println("----------------------------------------------------------------------------------");
		
		if (!ServerEnv.TEST)
			env.executePlanner();
		
	}
}
