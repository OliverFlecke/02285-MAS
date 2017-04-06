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

    protected BufferedReader 			serverIn;  
	protected PrintStream				serverOut;
    
	@Override
	public void init(String[] args) 
	{        
		super.init(args);
		
		requests	= new HashMap<String, ActRequest>();
			
		serverIn	= new BufferedReader(new InputStreamReader(System.in));		
		serverOut	= new PrintStream(new FileOutputStream(FileDescriptor.out));
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
//					logger.info(Arrays.toString(jointAction));
					
					serverOut.println(Arrays.toString(jointAction));
					
					String response = serverIn.readLine();
					
//					logger.info(response);
					
					if (response.contains("false"))
					{
						// Error handling
						logger.severe("Action failed on server");
					}					
	
	                for (ActRequest a: requests.values()) 
	                {
                        boolean success = executeAction(a.agName, a.action);
                        getEnvironmentInfraTier().actionExecuted(a.agName, a.action, success, a.infraData);
	                }
	                
	                updateAgsPercept();
				}
			} 
			catch (Exception e) 
			{
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
		
		String agName;
		Structure action;
		Object infraData;
		
		public ActRequest(String agName, Structure action, Object infraData) {
            this.agName = agName;
            this.action = action;
            this.infraData = infraData;
        }
	}
}
