package env;


import java.util.logging.Logger;

import env.model.DataWorldModel;
import env.model.WorldModel;
import env.planner.Planner;
import level.Level;
import level.action.Action;
import level.action.SkipAction;
import level.cell.*;

public class WorldEnv extends ServerEnv {

    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());
	
    private static DataWorldModel model;
    
    private static WorldEnv instance;
    
    private Planner planner;
    
    public WorldEnv()
    {
    	super();
    	
		instance = this;

		try {
			new WorldModel(Level.parse(serverIn));
			
			model = WorldModel.getInstance();

			updateNumberOfAgents();
			
			planner = new Planner();
			
			planner.plan();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.warning("Exception: " + e + " at init: " + e.getMessage());
		}
    }
    
    public static WorldEnv getInstance() 
    {
    	return instance;
    }
    
    public void executePlanner()
    {
    	for (int i = 0; ; i++) 
    	{
			for (Agent agent : model.getAgents())
			{
				Action action;
				if (i < planner.actions.get(agent.getNumber()).size())
					action = planner.actions.get(agent.getNumber()).get(i);
				else
					action = new SkipAction(agent.getLocation());
 				scheduleAction(action, agent.getNumber());
			}	
		}
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
}
