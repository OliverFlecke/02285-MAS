package env;

import java.util.logging.Logger;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.TimeSteppedEnvironment;
import jason.environment.grid.Location;

public class WorldEnv extends TimeSteppedEnvironment {

    private static final Logger logger = Logger.getLogger(WorldEnv.class.getName());
	
    WorldModel model;

    Term move = Literal.parseLiteral("do(move)");
    Term push = Literal.parseLiteral("do(push)");
    Term pull = Literal.parseLiteral("do(pull)");
    Term skip = Literal.parseLiteral("do(skip)");

	@Override
	public void init(String[] args) {
		// Initialize model
	}
	
	@Override
	protected void updateNumberOfAgents() {
        setNbAgs(model.getNbOfAgs());
	}

	@Override
	public boolean executeAction(String ag, Structure action) 
	{
		logger.info(ag + " doing: " + action);
		
        int agId = getAgNbFromName(ag);

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

	private int getAgNbFromName(String ag) {
		// TODO Auto-generated method stub
		return 0;
	}

    private String getAgNameFromID(int ag) {
		// TODO Auto-generated method stub
		return null;
	}

    public static Atom aEMPTY    = new Atom("empty");
    public static Atom aAGENT    = new Atom("agent");
    public static Atom aOBSTACLE = new Atom("obstacle");
    public static Atom aGOAL     = new Atom("goal");
    public static Atom aBOX      = new Atom("box");

    @Override
    public void updateAgsPercept() {
        for (int i = 0; i < model.getNbOfAgs(); i++) {
            updateAgPercept(i);
        }
    }

    private void updateAgPercept(int ag) {
        updateAgPercept(getAgNameFromID(ag), ag);
    }

	private void updateAgPercept(String agName, int ag) 
	{
		// Keep wall and goal percepts?
        clearPercepts(agName);

        Location l = model.getAgPos(ag);
        Literal p = ASSyntax.createLiteral("pos", 
                        ASSyntax.createNumber(l.x), 
                        ASSyntax.createNumber(l.y), 
                        ASSyntax.createNumber(getStep()));
        addPercept(agName, p);

        // what's around
        updateAgPercept(agName, ag, l.x - 1, l.y - 1);
        updateAgPercept(agName, ag, l.x - 1, l.y);
        updateAgPercept(agName, ag, l.x - 1, l.y + 1);
        updateAgPercept(agName, ag, l.x, l.y - 1);
        updateAgPercept(agName, ag, l.x, l.y);
        updateAgPercept(agName, ag, l.x, l.y + 1);
        updateAgPercept(agName, ag, l.x + 1, l.y - 1);
        updateAgPercept(agName, ag, l.x + 1, l.y);
        updateAgPercept(agName, ag, l.x + 1, l.y + 1);
    }

	private void updateAgPercept(String agName, int agId, int x, int y) 
	{
		if (model.isFree(x, y))
		{
			addPercept(agName, createCellPerception(aEMPTY, x, y));
		}
//		// Walls should be static
//		else if (model.hasObject(WorldModel.OBSTACLE, x, y)) 
//        {
//            addPercept(agName, createCellPerception(aOBSTACLE, x, y));
//        }
        else if (model.hasObject(WorldModel.AGENT, x, y))
        {
            addPercept(agName, createCellPerception(aAGENT, x, y));
        }
//		// Goals should be static
//        else if (model.hasObject(WorldModel.GOAL, x, y))
//        {
//        	addPercept(agName, createCellPerception(aGOAL, x, y));
//        }
        else if (model.hasObject(WorldModel.BOX, x, y))
        {
        	addPercept(agName, createCellPerception(aBOX, x, y));
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