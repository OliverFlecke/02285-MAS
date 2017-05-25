package env;


import java.util.List;
import java.util.logging.Logger;

import env.model.CellModel;
import env.model.WorldModel;
import env.planner.Planner;
import level.Level;
import level.action.Action;
import level.action.SkipAction;
import level.cell.Agent;
import logging.LoggerFactory;

public class WorldEnv extends ServerEnv {

    private static final Logger logger = LoggerFactory.getLogger(WorldEnv.class.getName());
	
    private static CellModel model;    
    private static WorldEnv instance;    
    private Planner planner;
    
    public WorldEnv()
    {
    	super();
    	
		instance = this;

		try 
		{			
			model = new WorldModel(Level.parse(serverIn));
			
			planner = new Planner();
			
			planner.plan();
		} 
		catch (Exception e) 
		{
			logger.warning("Exception: " + e + " at init: " + e.getMessage());
		}
    }
    
    public int getSolutionLength() {
    	return planner.getLastStep();
    }
  
    public void executePlanner()
    {
    	int finalStep = getSolutionLength();
    	
    	// Append skip actions to incomplete action lists
    	for (Agent agent : model.getAgents())
    	{
    		List<Action> actions = planner.getActions().get(agent.getNumber());
    		
    		while (actions.size() < finalStep)
    		{
    			actions.add(new SkipAction(agent.getLocation()));
    		}
    	}
    	
    	List<List<Action>> actions = planner.getActions();
    	
		String[] jointAction = new String[actions.size()];
    	
    	// Send joint action to server for each step
    	for (int step = 0; step < finalStep; step++)
    	{    		
    		for (int agNumber = 0; agNumber < actions.size(); agNumber++)
    		{
    			jointAction[agNumber] = actions.get(agNumber).get(step).toString();
    		}
    		sendJointAction(jointAction);
    	}
    }
    
    public static WorldEnv getInstance() {
    	return instance;
    }
}
