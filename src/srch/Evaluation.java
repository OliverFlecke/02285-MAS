package srch;

import java.util.*;

public abstract class Evaluation implements Comparator<Node> {

	public abstract int f(Node n);

	@Override
	public int compare(Node n1, Node n2) {
		return this.f(n1) - this.f(n2);
	}

	public static class AStar extends Evaluation {
		
		private Heuristic heuristic;
		
		public AStar(Heuristic h) {
			heuristic = h;
		}

		@Override
		public int f(Node n) {
			return n.getG() + heuristic.h(n);
		}

		@Override
		public String toString() {
			return "A* evaluation";
		}
	}

	public static class WeightedAStar extends Evaluation {
		
		private Heuristic heuristic;
		private int W;

		public WeightedAStar(Heuristic h, int W) {
			heuristic = h;
			this.W = W;
		}

		@Override
		public int f(Node n) {
			return n.getG() + W * heuristic.h(n);
		}

		@Override
		public String toString() {
			return String.format("WA*(%d) evaluation", W);
		}
	}

	public static class Greedy extends Evaluation {
		
		private Heuristic heuristic;
		
		public Greedy(Heuristic h) {
			heuristic = h;
		}

		@Override
		public int f(Node n) {
			return heuristic.h(n);
		}

		@Override
		public String toString() {
			return "Greedy evaluation";
		}
	}
}
