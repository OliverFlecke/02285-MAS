package env;


import java.util.logging.Logger;

import env.model.WorldModel;
import env.planner.Planner;
import jason.asSyntax.*;
import jason.environment.grid.Location;
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
    
    @Override
    public void init(String[] args) 
    {
    	super.init(args);
    	
		instance = this;

		try {
			new WorldModel(Level.parse(serverIn));
			
			model = WorldModel.getInstance();
			
			Planner.plan();

			updateNumberOfAgents();

			updateInitialAgsPercept();
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
    protected void updateAgsPercept() 
    {	
    	clearAllPercepts();
    	
    	updateInitialAgsPercept();
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
    
	private boolean notInitialized = true;
    private synchronized void updateInitialAgsPercept()
    {
    	for (int x = 0; x < model.getWidth(); x++)
    	{
    		for (int y = 0; y < model.getHeight(); y++)
    		{
				addModelPercepts(x, y);
    		}
    	}

    	// Add agent specific information to each agent
    	for (Agent agent : model.getAgents())
    	{
    		if (notInitialized) // We just need to make sure the agents are only created once
    			addPercept("initializer", Literal.parseLiteral("create_agent(" + agent.getName() + ")"));
    		
    		addPercept(agent.getName(), createPosPerception(agent.getLocation().x, agent.getLocation().y));
    		addPercept(agent.getName(), createColorPerception(agent.getColor()));
    		addPercept(agent.getName(), createStepPerception());
    	}
    	notInitialized = false;
    	setNbAgs(model.getAgents().length);
    }

    /**
     * Add percepts for a given location based on the model.
     * A cell can have more than one object.
     * @param x
     * @param y
     */
	private void addModelPercepts(int x, int y) 
	{
		if (model.hasObject(WorldModel.GOAL, x, y))
        {
        	addPercept(createGoalPerception(x, y));
        }
		
		if (model.isFree(x, y))
		{
        	addPercept(createFreePerception(x, y));
		}
		else if (model.hasObject(WorldModel.AGENT, x, y))
        {
            addPercept(createAgentPerception(x, y));
        }
        else if (model.hasObject(WorldModel.BOX, x, y))
        {
        	addPercept(createBoxPerception(x, y));
        }
	}
	
	public void addAgentPercept(String agentName, Literal percept)
	{
		addPercept(agentName, percept);
	}
    
    public static Literal createBoxPerception(int x, int y)
    {
    	Box box = model.getBox(x, y);
    	return ASSyntax.createLiteral("box", 
    			ASSyntax.createAtom(box.getColor()),
    			ASSyntax.createAtom(Character.toString(box.getLetter())),
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y)); 
    }
    
    public static Literal createGoalPerception(int x, int y)
    {
    	Goal goal = model.getGoal(x, y);
    	return ASSyntax.createLiteral("goal", 
    			ASSyntax.createAtom(Character.toString(goal.getLetter())),
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y)); 
    }
    
    public static Literal createAgentPerception(int x, int y)
    {
    	Agent agent = model.getAgent(x, y);
    	return ASSyntax.createLiteral("agent",
    			ASSyntax.createAtom(agent.getColor()),
    			ASSyntax.createNumber(x),
    			ASSyntax.createNumber(y));
    }
    
    public static Literal createPosPerception(int x, int y)
    {
    	return ASSyntax.createLiteral("pos",
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y)); 
    }
    
    public static Literal createColorPerception(String color)
    {
    	return ASSyntax.createLiteral("color", 
    			ASSyntax.createAtom(color));
    }
    
    public static Literal createFreePerception(int x, int y)
    {
    	return ASSyntax.createLiteral("free",
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y)); 
    }
    
    public static Literal createMovePerception(Location l)
    {
    	return ASSyntax.createLiteral("help",
                ASSyntax.createNumber(l.x),
                ASSyntax.createNumber(l.y)); 
    }
    
    public static Literal createMoveBoxPerception(Location box, Location to)
    {
    	return ASSyntax.createLiteral("help",
                ASSyntax.createNumber(box.x),
                ASSyntax.createNumber(box.y),
                ASSyntax.createNumber(to.x),
                ASSyntax.createNumber(to.y)); 
    }
    
    
    
    public static Literal createStepPerception()
    {
    	return ASSyntax.createLiteral("step", ASSyntax.createNumber(WorldModel.getInstance().getStep()));
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
