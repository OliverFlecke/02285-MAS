package mas;

import java.util.LinkedList;

public class Command {
	// Order of enum important for determining opposites
	public static enum Dir {
		N, W, E, S
	};

	public static enum Type {
		Move, Push, Pull
	};

	static {
		LinkedList<Command> cmds = new LinkedList<Command>();
		for (Dir d : Dir.values()) {
			cmds.add(new Command(d));
		}

		for (Dir d1 : Dir.values()) {
			for (Dir d2 : Dir.values()) {
				if (!Command.isOpposite(d1, d2)) {
					cmds.add(new Command(Type.Push, d1, d2));
				}
			}
		}
		for (Dir d1 : Dir.values()) {
			for (Dir d2 : Dir.values()) {
				if (d1 != d2) {
					cmds.add(new Command(Type.Pull, d1, d2));
				}
			}
		}

		every = cmds.toArray(new Command[0]);
	}

	public final static Command[] every;

	private static boolean isOpposite(Dir d1, Dir d2) {
		return d1.ordinal() + d2.ordinal() == 3;
	}

	public final Type actionType;
	public final Dir dir1;
	public final Dir dir2;

	/**
	 * Constructor for moving in some direction
	 * @param d
	 */
	public Command(Dir d) {
		actionType = Type.Move;
		dir1 = d;
		dir2 = null;
	}

	public Command(Type t, Dir d1, Dir d2) {
		actionType = t;
		dir1 = d1;
		dir2 = d2;
	}

	public static int dirToRowChange(Dir d) {
		// South is down one row (1), north is up one row (-1).
		switch (d) {
		case S:
			return 1;
		case N:
			return -1;
		default:
			return 0;
		}
	}

	public static int dirToColChange(Dir d) {
		// East is right one column (1), west is left one column (-1).
		switch (d) {
		case E:
			return 1;
		case W:
			return -1;
		default:
			return 0;
		}
	}

	public String toString() {
		if (actionType == Type.Move)
			return actionType.toString() + "(" + dir1 + ")";

		return actionType.toString() + "(" + dir1 + "," + dir2 + ")";
	}

	public String toActionString() {
		return "[" + this.toString() + "]";
	}

}
