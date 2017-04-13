package srch;

import java.util.*;
import java.util.function.Function;

public abstract class Evaluation implements Comparator<Node> {

	public abstract int f(Node n);

	@Override
	public int compare(Node n1, Node n2) {
		return this.f(n1) - this.f(n2);
	}

	public static class AStar extends Evaluation {
		
		private Function<Node, Integer> heuristic;
		
		public AStar(Function<Node, Integer> h) {
			heuristic = h;
		}

		@Override
		public int f(Node n) {
			return n.g() + heuristic.apply(n);
		}

		@Override
		public String toString() {
			return "A* evaluation";
		}
	}

	public static class WeightedAStar extends Evaluation {
		
		private Function<Node, Integer> heuristic;
		private int W;

		public WeightedAStar(Function<Node, Integer> h, int W) {
			heuristic = h;
			this.W = W;
		}

		@Override
		public int f(Node n) {
			return n.g() + W * heuristic.apply(n);
		}

		@Override
		public String toString() {
			return String.format("WA*(%d) evaluation", W);
		}
	}

	public static class Greedy extends Evaluation {
		
		private Function<Node, Integer> heuristic;
		
		public Greedy(Function<Node, Integer> h) {
			heuristic = h;
		}

		@Override
		public int f(Node n) {
			return heuristic.apply(n);
		}

		@Override
		public String toString() {
			return "Greedy evaluation";
		}
	}
}
