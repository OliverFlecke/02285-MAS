package env;


import java.util.logging.Logger;

import env.model.WorldModel;
import env.planner.Planner;
import jason.asSyntax.*;
import level.Level;
import level.cell.*;

public class WorldEnv extends ServerEnv {

    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());

    private static final String MOVE = "move";
    private static final String PUSH = "push";
    private static final String PULL = "pull";
    private static final String SKIP = "skip";   
	
    private static WorldModel model;
    
    private static WorldEnv instance;
    
    public WorldEnv()
    {
    	super();
    	
		instance = this;

		try {
			new WorldModel(Level.parse(serverIn));
			
			model = WorldModel.getInstance();

			updateNumberOfAgents();
			
			Planner.plan();
		} 
		catch (Exception e) 
		{
			logger.warning("Exception: " + e + " at init: " + e.getMessage());
		}
    }
    
    public static WorldEnv getInstance() 
    {
    	return instance;
    }
    
    @Override
    protected void updateNumberOfAgents() {
		setNbAgs(model.getNbAgs());
    }
	
	@Override
	protected int getAgentIdByName(String name) {
		for (Agent agent : model.getAgents())
			if (agent.getName().equals(name))
				return agent.getNumber();
		return 0; // Default value
	}

	@Override
	public boolean executeAction(String agentName, Structure action) 
	{
		int agentId = getAgentIdByName(agentName);
		
        switch(action.getFunctor())
        {
        case MOVE: return model.move(action.getTerm(0), agentId);
        case PUSH: return model.push(action.getTerm(0), action.getTerm(1), agentId);
        case PULL: return model.pull(action.getTerm(0), action.getTerm(1), agentId);
        case SKIP: return true;
        default: 
            logger.warning("** Action not implemented: " + action);
        	return true;
        }
	}
    
    @Override
	public String toString(Structure action)
	{
		switch(action.getFunctor())
		{
		case MOVE: return "Move(" + toString(action.getTerm(0)) + ")";
		case PUSH: return "Push(" + toString(action.getTerm(0)) + "," 
								  + toString(action.getTerm(1)) + ")";
		case PULL: return "Pull(" + toString(action.getTerm(0)) + "," 
		  						  + toString(action.getTerm(1)) + ")";
		default  : return "NoOp";
		}
	}

	public static String toString(Term dir)
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
}
