package env;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import env.model.WorldModel;
import jason.asSyntax.Structure;
import level.action.Action;
import logging.LoggerFactory;

/**
 * Derived from TimeSteppedEnvironment
 */
public class ServerEnv {
	
    private static final Logger logger = LoggerFactory.getLogger(ServerEnv.class.getName());
    
    public static final boolean TEST = false;

	private int 						nbAgs;
	private HashMap<Integer, Action> 	requests;
	private String[] 					jointAction;

    protected BufferedReader 			serverIn;  
	protected PrintStream				serverOut;
    
	public ServerEnv()
	{		
		requests	= new HashMap<Integer, Action>();

		serverIn	= new BufferedReader(new InputStreamReader(System.in));		
		serverOut	= new PrintStream(new FileOutputStream(FileDescriptor.out));
		
		if (TEST)
		{
			try {
				serverIn = new BufferedReader(new FileReader(new File("levels\\compF16levels\\SADangerBot.lvl")));
//				serverIn = new BufferedReader(new FileReader(new File("levels\\multi_agent\\short_solution\\MARageQuit.lvl")));
//				serverIn = new BufferedReader(new FileReader(new File("levels\\single_agent\\short_solution\\SARageQuit.lvl")));
//				serverIn = new BufferedReader(new FileReader(new File("levels\\SAtest.lvl")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setNbAgs(int n) {
		nbAgs = n;
		jointAction = new String[nbAgs];
	}
	
	public void scheduleAction(final Action action, final int agentId) 
	{
		synchronized (requests)
		{
			requests.put(agentId, action);
			
			try 
			{
				if (requests.size() >= nbAgs)
				{
					for (Entry<Integer, Action> entry : requests.entrySet())
					{
						jointAction[entry.getKey()] = entry.getValue().toString();
					}
					requests.clear();
					
					serverOut.println(Arrays.toString(jointAction));
					
					WorldModel.getInstance().nextStep();
					
					if (!TEST)
					{
						String response = serverIn.readLine();
						
//						logger.info(response);
						
						if (response.contains("false"))
						{
							// Error handling
							logger.severe("Action failed on server");
						}
					}
					
					// Send step percept when actions have been executed?
				}
			} 
			catch (Exception e) 
			{
				logger.warning("Error: scheduleAction");
				e.printStackTrace();
			}
		}
	}
	
	class ActRequest {
		
		String agentName;
		Structure action;
		Object infraData;
		
		public ActRequest(String agentName, Structure action, Object infraData) {
            this.agentName = agentName;
            this.action = action;
            this.infraData = infraData;
        }
	}
}
