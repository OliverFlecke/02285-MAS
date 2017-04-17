package env;


import java.util.logging.Logger;

import env.model.WorldModel;
import jason.asSyntax.*;
import lvl.cell.*;
import lvl.Level;
public class WorldEnv extends ServerEnv {

    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());

    private static final String MOVE = "move";
    private static final String PUSH = "push";
    private static final String PULL = "pull";
    private static final String SKIP = "skip";   
	
    private static WorldModel model;
    
    @Override
    public void init(String[] args) 
    {
    	super.init(args);

		try {
			new WorldModel(Level.parse(serverIn));
			
			model = WorldModel.getInstance();

			updateNumberOfAgents();

			updateInitialAgsPercept();
		} 
		catch (Exception e) 
		{
			logger.warning("Exception: " + e + " at init: " + e.getMessage());
		}
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
		return 0;
	}

	@Override
	public boolean executeAction(String ag, Structure action) 
	{
//		logger.info(ag + " doing: " + action);
		
        int agId = (int) ((NumberTermImpl) action.getTerm(0)).solve();
        
        switch(action.getFunctor())
        {
        case MOVE: return model.move(action.getTerm(1), agId);
        case PUSH: return model.push(action.getTerm(1), action.getTerm(2), agId);
        case PULL: return model.pull(action.getTerm(1), action.getTerm(2), agId);
        case SKIP: return true;
        default: 
            logger.warning("** Action not implemented: " + action);
        	return true;
        }
	}
    
    private void updateInitialAgsPercept()
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
    		addPercept("initializer", Literal.parseLiteral("create_agent(" + agent.getName() + ")"));
    		
    		addPercept(agent.getName(), createPosPerception(agent.getLocation().x, agent.getLocation().y));
    		addPercept(agent.getName(), createColorPerception(agent.getColor()));
    		addPercept(agent.getName(), createIdPerception(agent));
    	}
    	
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
		if (model.hasObject(x, y, WorldModel.GOAL))
        {
        	addPercept(createGoalPerception(x, y));
        }
		
		if (model.isFree(x, y))
		{
        	addPercept(createFreePerception(x, y));
		}
		else if (model.hasObject(x, y, WorldModel.AGENT))
        {
            addPercept(createAgentPerception(x, y));
        }
        else if (model.hasObject(x, y, WorldModel.BOX))
        {
        	addPercept(createBoxPerception(x, y));
        }
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
    
    public static Literal createIdPerception(Agent agent)
    {
    	return ASSyntax.createLiteral("id", ASSyntax.createNumber(agent.getNumber()));
    }
	
    @Override
	public String toString(Structure action)
	{
		switch(action.getFunctor())
		{
		case MOVE: return "Move(" + toString(action.getTerm(1)) + ")";
		case PUSH: return "Push(" + toString(action.getTerm(1)) + "," 
								  + toString(action.getTerm(2)) + ")";
		case PULL: return "Pull(" + toString(action.getTerm(1)) + "," 
		  						  + toString(action.getTerm(2)) + ")";
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
