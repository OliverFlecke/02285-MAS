package mas;

import static org.junit.Assert.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import logging.LoggerFactory;

public class Tester {
	
	private static Logger logger = LoggerFactory.getLogger(Tester.class.getName());

	private static final long allowedTime = 10000;
	
	@BeforeClass
	public static void before()
	{
		logger.setLevel(Level.WARNING);
	}
	
	/**
	 * Tests a level
	 * @param level path to the level
	 */
	public static void testLevel(String level) 
	{	
		logger.fine("Testing level: " + level);
		
		String command = "java -jar server.jar -l levels\\" + level + " -c \"java -jar client.jar\"";
		
		Runtime rt = Runtime.getRuntime();
		
		Process proc = null;
		
		try {
			proc = rt.exec(command);
		} catch (IOException e2) {
			logger.warning("Could not start process");
			e2.printStackTrace();
			fail();
			return;
		}

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		try 
		{
			String s = null;
			long startTime = System.currentTimeMillis();

			for (long time = 0; time < allowedTime; time = System.currentTimeMillis() - startTime) 
			{
				if (stdInput.ready()) s = stdInput.readLine();
				
				if (s != null && s.equals("success")) 
				{
					logger.fine("Level solved!");
					return;
				}
			}
			
			logger.warning("Unable to solve level: " + level);
			
			fail();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
			fail();
		}
	}
}