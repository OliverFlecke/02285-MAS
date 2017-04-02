package env;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

/**
 * Derived from TimeSteppedEnvironment
 */
public class WorldEnv extends Environment {
	
    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());

    private static final String MOVE = "move";
    private static final String PUSH = "push";
    private static final String PULL = "pull";
    private static final String SKIP = "skip";   

    private BufferedReader 				serverMessages;  
    private WorldModel 					model;
	private int 						nbAgs;	  
	private HashMap<String, ActRequest> requests;	
	private String[] 					jointAction;	
    
	@Override
	public void init(String[] args) 
	{        
		super.init(args);
		
		try {			
			serverMessages = new BufferedReader(new InputStreamReader(System.in));
			
			model = new WorldModel(Level.parse(serverMessages));
			
			nbAgs = model.getNbOfAgs();
			
			requests = new HashMap<String, ActRequest>();
			
			jointAction = new String[nbAgs];
			
			updateInitialAgsPercept();
		} 
		catch (IOException e) 
		{
			logger.warning("Exception: " + e + " at init: " + e.getMessage());
		}
	}
	
	@Override
	public void scheduleAction(final String agName, final Structure action, final Object infraData) 
	{
        int agId = getAgentIdByName(agName);
        
        synchronized (jointAction) 
        {
			jointAction[agId] = actionToCommand(action);
		}
        
        ActRequest newRequest = new ActRequest(agName, action, infraData);
        
        synchronized (requests) 
        {        	
			requests.put(agName, newRequest);

			try {
				if (requests.size() >= nbAgs)
				{
//					System.err.println(Arrays.toString(jointAction));
					System.out.println(Arrays.toString(jointAction));
					
//					String response = serverMessages.readLine();
//					
//					if (response.contains("false"))
//					{
//						System.err.println(response);
//						// Error handling
//						logger.severe("Action failed on server");
//					}					
	
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

	@Override
	public boolean executeAction(String ag, Structure action) 
	{
		logger.info(ag + " doing: " + action);
		
        int agId = 0;
        
        switch(action.getFunctor())
        {
        case MOVE: return model.move(action.getTerm(0), agId);
        case PUSH: return model.push(action.getTerm(0), action.getTerm(1), agId);
        case PULL: return model.pull(action.getTerm(0), action.getTerm(1), agId);
        case SKIP: return true;
        default: 
            logger.warning("** Action not implemented: " + action);
        	return true;
        }
	}
	
	private int getAgentIdByName(String name) {
		return 0;
	}

    public static final Atom aEMPTY    = new Atom("empty");
    public static final Atom aAGENT    = new Atom("agent");
    public static final Atom aOBSTACLE = new Atom("obstacle");
    public static final Atom aGOAL     = new Atom("goal");
    public static final Atom aBOX      = new Atom("box");
    
    private void updateInitialAgsPercept()
    {
    	for (int x = 0; x < model.getWidth(); x++)
    	{
    		for (int y = 0; y < model.getHeight(); y++)
    		{
    			addModelPercepts(x, y);
    		}
    	}
    }

    /**
     * Add percepts for a given location based on the model.
     * A cell can have more than one object.
     * @param x
     * @param y
     */
	private void addModelPercepts(int x, int y) 
	{
		// TODO: Can be optimized
		if (model.isFree(x, y))
		{
			addPercept(createCellPerception(aEMPTY, x, y));
		}
		if (model.hasObject(WorldModel.OBSTACLE, x, y)) 
        {
            addPercept(createCellPerception(aOBSTACLE, x, y));
        }
        if (model.hasObject(WorldModel.AGENT, x, y))
        {
            addPercept(createCellPerception(aAGENT, x, y));
        }
        if (model.hasObject(WorldModel.GOAL, x, y))
        {
        	addPercept(createCellPerception(aGOAL, x, y));
        }
        if (model.hasObject(WorldModel.BOX, x, y))
        {
        	addPercept(createCellPerception(aBOX, x, y));
        }
	}
    
    public static Literal createCellPerception(Atom obj, int x, int y) 
    {
        return ASSyntax.createLiteral("cell", obj,
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y)); 
    }
	
	public static String actionToCommand(Structure action)
	{
		switch(action.getFunctor())
		{
		case MOVE: return "Move(" + dirToCommand(action.getTerm(0)) + ")";
		case PUSH: return "Push(" + dirToCommand(action.getTerm(0)) + "," 
								  + dirToCommand(action.getTerm(1)) + ")";
		case PULL: return "Pull(" + dirToCommand(action.getTerm(0)) + "," 
		  						  + dirToCommand(action.getTerm(1)) + ")";
		default  : return "NoOp";
		}
	}
	
	public static String dirToCommand(Term dir)
	{
		switch(dir.toString())
		{
		case WorldModel.UP   : return "N";
		case WorldModel.DOWN : return "S";
		case WorldModel.LEFT : return "W";
		case WorldModel.RIGHT: return "E";
		}
		return "";
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
