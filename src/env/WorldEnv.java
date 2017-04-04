package env;

import java.io.IOException;
import java.util.logging.Logger;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;

public class WorldEnv extends ServerEnv {

    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());

    private static final String MOVE = "move";
    private static final String PUSH = "push";
    private static final String PULL = "pull";
    private static final String SKIP = "skip";   
	
    private WorldModel 					model;
    
    @Override
    public void init(String[] args) 
    {
    	super.init(args);
    	
		try {
			model = new WorldModel(Level.parse(serverIn));
			
			updateNumberOfAgents();
			
			updateInitialAgsPercept();
			
//			executeAction("agent", Structure.parse(PUSH + "(right,right)"));
		} 
		catch (IOException e) 
		{
			logger.warning("Exception: " + e + " at init: " + e.getMessage());
		}
    }
    
    @Override
    protected void updateNumberOfAgents() {
		setNbAgs(model.getNbOfAgs());    	
    }
	
	@Override
	protected int getAgentIdByName(String name) {
		return 0;
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
