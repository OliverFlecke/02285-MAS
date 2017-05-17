package env;


import java.util.logging.Logger;

import env.model.CellModel;
import env.model.WorldModel;
import env.planner.Planner;
import level.Level;
import level.action.Action;
import level.action.SkipAction;
import level.cell.*;
import logging.LoggerFactory;

public class WorldEnv extends ServerEnv {

    private static final Logger logger = LoggerFactory.getLogger(WorldEnv.class.getName());
	
    private static CellModel model;
    
    private static WorldEnv instance;
    
    public Planner planner;
    
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
//			e.printStackTrace();
			logger.warning("Exception: " + e + " at init: " + e.getMessage());
		}
    }
    
    public static WorldEnv getInstance() 
    {
    	return instance;
    }
  
    public void executePlanner()
    {
    	int steps = planner.getLastStep();
    	
    	for (int i = 0; i < steps; i++) 
    	{
			for (Agent agent : model.getAgents())
			{
				Action action;
				if (i < this.planner.getActions().get(agent.getNumber()).size())
					action = this.planner.getActions().get(agent.getNumber()).get(i);
				else
					action = new SkipAction(agent.getLocation());
 				scheduleAction(action, agent.getNumber());
			}	
		}
    }
 
    protected void updateNumberOfAgents() {
		setNbAgs(model.getNbAgs());
    }

	protected int getAgentIdByName(String name) {
		for (Agent agent : model.getAgents())
			if (agent.getName().equals(name))
				return agent.getNumber();
		return 0; // Default value
	}
}
