package mas;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import logging.LoggerFactory;

public class Tester {
	
	private static Logger logger = LoggerFactory.getLogger(Tester.class.getName());

	private static final long MAX_TIME = 30;
	
	@BeforeClass
	public static void before()
	{
		logger.setLevel(Level.INFO);
	}
	
	@AfterClass
	public static void after()
	{
		try {
			Runtime.getRuntime().exec("taskkill /F /IM java.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testLevel(String level)
	{
		testLevel(level, MAX_TIME);
	}
	
	/**
	 * Tests a level
	 * @param level path to the level
	 * @throws Exception 
	 */
	public static void testLevel(String level, long allowedTime)  
	{	
		logger.fine("Testing level: " + level);
		
		level = level + ".lvl";
		
		String command = "java -jar server.jar -l levels\\" + level + " -c \"java -Xmx4g -jar client.jar\"";
		
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
		BufferedReader errorInput = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		try 
		{
			String input = null, error = null;
			long startTime = System.currentTimeMillis();

			for (long time = 0; time < allowedTime * 1000; time = System.currentTimeMillis() - startTime) 
			{
				if (stdInput.ready()) 
				{
					input = stdInput.readLine();
					
					if (input != null && input.equals("success")) 
					{
						logger.fine("Level solved!");
						
						proc.destroyForcibly();
						
						return;
					}
				}
				if (errorInput.ready()) 
				{
					error = errorInput.readLine();
					
					if (error.toLowerCase().contains("exception") || error.toLowerCase().contains("does not exist")) 
					{
						logger.warning("Got an exception:\n" + error);
						
						while (errorInput.ready())
						{
							error = errorInput.readLine();
							
							logger.warning(error);
						}
						
//						Runtime.getRuntime().exec("taskkill /F /IM java.exe");
						
						throw new UnsupportedOperationException("See command line for details");
					}
					else if (error.contains("Result"))
					{						
						System.out.println(error);
					}
				}
			}
			
			logger.warning("Unable to solve level: " + level);

//			Runtime.getRuntime().exec("taskkill /F /IM java.exe");
			
			fail("Unable to solve level");
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
			fail();
		}
	}
}
