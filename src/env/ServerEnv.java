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
import java.util.logging.Logger;

import jason.asSyntax.Structure;
import jason.environment.Environment;

/**
 * Derived from TimeSteppedEnvironment
 */
public class ServerEnv extends Environment {
	
    private static final Logger logger = Logger.getLogger(ServerEnv.class.getName());
    
    private static final boolean TEST = false;

	private int 						nbAgs;	  
	private HashMap<String, ActRequest> requests;	
	private String[] 					jointAction;

    protected BufferedReader 			serverIn;  
	protected PrintStream				serverOut;
    
	@Override
	public void init(String[] args) 
	{        
		super.init(args);
		
		requests	= new HashMap<String, ActRequest>();
			
		serverIn	= new BufferedReader(new InputStreamReader(System.in));		
		serverOut	= new PrintStream(new FileOutputStream(FileDescriptor.out));
		
		if (TEST)
		{
			try {
				serverIn = new BufferedReader(new FileReader(new File("levels/single_agent/easy/SACrunch.lvl")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setNbAgs(int n) {
		nbAgs = n;
		jointAction = new String[nbAgs];
	}
	
	// Override
	protected void updateNumberOfAgents() {
		
	}
	
	// Override
	protected void updateAgsPercept() {
		
	}
	
	// Override
	protected int getAgentIdByName(String name) {
		return 0;
	}
	
	// Override
	protected String toString(Structure action) {
		return "";
	}
	
	@Override
	public void scheduleAction(final String agentName, final Structure action, final Object infraData) 
	{
//        int agentId = getAgentIdByName(agentName);
//        
//        synchronized (jointAction) 
//        {
//			jointAction[agentId] = toString(action);
//		}
        
        ActRequest newRequest = new ActRequest(agentName, action, infraData);
        
        synchronized (requests) 
        {        	
			requests.put(agentName, newRequest);
			try {
				if (requests.size() >= nbAgs)
				{								
					for (ActRequest request : requests.values()) 
					{
						boolean success = executeAction(request.agentName, request.action);
						getEnvironmentInfraTier().actionExecuted(request.agentName, request.action, success, request.infraData);
						if (success)
							jointAction[getAgentIdByName(request.agentName)] = this.toString(request.action);
						else 
							jointAction[getAgentIdByName(request.agentName)] = this.toString(new Structure("NoOp"));
					}
					serverOut.println(Arrays.toString(jointAction));
					requests.clear();
					updateAgsPercept();
					
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
	
	@Override
	public void stop() {
		super.stop();
		serverOut.close();
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
