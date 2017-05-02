package env;


import java.util.logging.Logger;

import env.model.WorldModel;
import env.planner.Planner;
import level.Level;
import level.Actions.*;
import level.cell.*;

public class WorldEnv extends ServerEnv {

    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());
	
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

	public boolean canExecute(Action action, int agentId) 
	{		
        switch(action.getType())
        {
        case MOVE: return model.canMove((MoveAction) action, agentId);
        case PUSH: return model.canPush((PushAction) action, agentId);
        case PULL: return model.canPull((PullAction) action, agentId);
        case SKIP: return true;
        }
        throw new UnsupportedOperationException("Invalid action: " + action.getType());        
	}
	
	public void doExecute(Action action, int agentId)
	{
        switch(action.getType())
        {
        case MOVE: model.doMove((MoveAction) action, agentId); return;
        case PUSH: model.doPush((PushAction) action, agentId); return;
        case PULL: model.doPull((PullAction) action, agentId); return;
		case SKIP:                                             return;
        }
        throw new UnsupportedOperationException("Invalid action: " + action.getType());    
	}
}
