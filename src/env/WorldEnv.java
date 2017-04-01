package env;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.TimeSteppedEnvironment;

public class WorldEnv extends TimeSteppedEnvironment {

    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());
	
    private BufferedReader serverMessages;
    
    WorldModel model;

    Term move = Literal.parseLiteral("do(move)");
    Term push = Literal.parseLiteral("do(push)");
    Term pull = Literal.parseLiteral("do(pull)");
    Term skip = Literal.parseLiteral("do(skip)");

	@Override
	public void init(String[] args) 
	{
        super.init(new String[] { "1000" } ); // set step timeout
        
		try {
			serverMessages = new BufferedReader(new InputStreamReader(System.in));
			
			model = new WorldModel(Level.parse(serverMessages));
			
			updateNumberOfAgents();	
			
			updateInitialAgsPercept();
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
	public boolean executeAction(String ag, Structure action) 
	{
		logger.info(ag + " doing: " + action);
		
        int agId = 0;

        if (action.equals(move)) {
            return model.move(WorldModel.Direction.N, agId);
        } else if (action.equals(push)) {
            return model.push(WorldModel.Direction.N, WorldModel.Direction.N, agId);
        } else if (action.equals(pull)) {
            return model.pull(WorldModel.Direction.N, WorldModel.Direction.N, agId);
		} else if (action.equals(skip)) {
			logger.info("agent " + ag + " skips!");
        } else {
            logger.warning("executing: " + action + ", but not implemented!");
        }        
		return true;
	}

    public static Atom aEMPTY    = new Atom("empty");
    public static Atom aAGENT    = new Atom("agent");
    public static Atom aOBSTACLE = new Atom("obstacle");
    public static Atom aGOAL     = new Atom("goal");
    public static Atom aBOX      = new Atom("box");

    @Override
    public void updateAgsPercept() 
    {	
    	clearAllPercepts();
    	
    	// Should probably use a common belief base for all agents
    	// where walls and goals are static.
    	updateInitialAgsPercept();
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
    }

    /**
     * Add percepts for a given location based on the model.
     * A cell can have more than one object.
     * @param x
     * @param y
     */
	private void addModelPercepts(int x, int y) 
	{
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
    protected void stepFinished(int step, long elapsedTime, boolean byTimeout) {
    	// TODO Auto-generated method stub
    }
}