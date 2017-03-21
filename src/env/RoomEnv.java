package env;

import jason.asSyntax.*;

public class RoomEnv extends jason.environment.Environment {

	Literal ld = Literal.parseLiteral("locked(door)");

	public void init(String[] args) {

		addPercept(ld); // initial perception: door locked

	}

	public boolean executeAction(String ag, Structure act) {

		if (act.getFunctor().equals("unlock"))

			clearPercepts();

		if (act.getFunctor().equals("lock"))

			addPercept(ld);

		return true;

	}

}