package env;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
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

	private int 						nbAgs;	  
	private HashMap<String, ActRequest> requests;	
	private String[] 					jointAction;
	private boolean 					first = true;

    protected BufferedReader 			serverMessages;  
    
	@Override
	public void init(String[] args) 
	{        
		super.init(args);
			
		serverMessages = new BufferedReader(new InputStreamReader(System.in));		
		
		requests = new HashMap<String, ActRequest>();
	}
	
	public void setNbAgs(int n) {
		nbAgs = n;
		jointAction = new String[nbAgs];
	}
	
	// Override
	protected void updateNumberOfAgents() {
		
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
	public void scheduleAction(final String agName, final Structure action, final Object infraData) 
	{
        int agId = getAgentIdByName(agName);
        
        synchronized (jointAction) 
        {
			jointAction[agId] = toString(action);
		}
        
        ActRequest newRequest = new ActRequest(agName, action, infraData);
        
        synchronized (requests) 
        {        	
			requests.put(agName, newRequest);

			try {
				if (requests.size() >= nbAgs)
				{
					if (first) 
					{
						// RunCentralisedMAS prints to System.out after environment init
						System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
						first = false;
					}
					System.err.println(Arrays.toString(jointAction));
					System.out.println(Arrays.toString(jointAction));
					
					String response = serverMessages.readLine();
					
					if (response.contains("false"))
					{
						System.err.println(response);
						// Error handling
						logger.severe("Action failed on server");
					}					
	
	                for (ActRequest a: requests.values()) 
	                {
                        a.success = executeAction(a.agName, a.action);
                        getEnvironmentInfraTier().actionExecuted(a.agName, a.action, a.success, a.infraData);
	                }
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	class ActRequest {
		String agName;
		Structure action;
		Object infraData;
		boolean success;
		
		public ActRequest(String ag, Structure act, Object data) {
            agName = ag;
            action = act;
            infraData = data;
        }
		
        public boolean equals(Object obj) {
            return agName.equals(obj);
        }
        
        public int hashCode() {
            return agName.hashCode();
        }
	}
}
