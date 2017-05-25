package mas;

import java.text.DecimalFormat;

import env.WorldEnv;

public class Runner {

	private static DecimalFormat df = new DecimalFormat("0.0000");
	
	public static void main(String[] args) 
	{			
		try 
		{
			long startTime = System.nanoTime();
			
			WorldEnv env = new WorldEnv();
			
			long endTime = System.nanoTime();
			
			String duration = df.format( (endTime - startTime) / 1000000000.0 );
			
			System.err.println("Result: " + duration + " " + env.getSolutionLength());
			
			if (args.length == 0)
			{
				env.executePlanner();				
			}
		}
		catch (Throwable e) { e.printStackTrace(); }		
	}
}
