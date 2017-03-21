package mas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Client {

	public Node initialState;

	/**
	 * Reads in the level, which is sent through the bufferedReader object
	 * @param serverMessages
	 * @throws Exception
	 */
	public Client(BufferedReader serverMessages) throws Exception {
		// Read lines specifying colors
		String line = serverMessages.readLine();
		if (line.matches("^[a-z]+:\\s*[0-9A-Z](\\s*,\\s*[0-9A-Z])*\\s*$")) {
			System.err.println("Error, client does not support colors.");
			System.exit(1);
		}

		int row = 0;
		this.initialState = new Node(null);

		List<List<Boolean>> wallsList = new ArrayList<List<Boolean>>();
		List<List<Character>> boxesList = new ArrayList<List<Character>>();
		List<List<Character>> goalsList = new ArrayList<List<Character>>();

		while (!line.equals("")) {
			// Add rows
			ArrayList<Boolean> wallsRow = new ArrayList<Boolean>();
			ArrayList<Character> boxesRow = new ArrayList<Character>();
			ArrayList<Character> goalsRow = new ArrayList<Character>();
			wallsList.add(wallsRow);
			boxesList.add(boxesRow);
			goalsList.add(goalsRow);
			// Add cols
			for (int col = 0; col < line.length(); col++) {
				char chr = line.charAt(col);

				if (chr == '+') { // Wall.
					wallsRow.add(true);
					boxesRow.add('\0');
					goalsRow.add('\0');
				} else if ('0' <= chr && chr <= '9') { // Agent.
					this.initialState.agentRow = row;
					this.initialState.agentCol = col;
					wallsRow.add(false);
					boxesRow.add('\0');
					goalsRow.add('\0');
				} else if ('A' <= chr && chr <= 'Z') { // Box.
					wallsRow.add(false);
					boxesRow.add(chr);
					goalsRow.add('\0');
				} else if ('a' <= chr && chr <= 'z') { // Goal.
					wallsRow.add(false);
					boxesRow.add('\0');
					goalsRow.add(chr);
				} else if (chr == ' ') { // Free space.
					wallsRow.add(false);
					boxesRow.add('\0');
					goalsRow.add('\0');
				} else {
					System.err.println("Error, read invalid level character: " + (int) chr);
					System.exit(1);
				}
			}
			line = serverMessages.readLine();
			row++;
		}

		int maxRows = wallsList.size();
		int maxCols = 0;		

		for (int i = 0; i < maxRows; i++) {
			int colSize = wallsList.get(i).size();
			if (colSize > maxCols) {
				maxCols = colSize;
			}
		}

		Node.MAX_ROW = maxRows;
		Node.MAX_COL = maxCols;

		boolean[][] walls = new boolean[maxRows][maxCols];
		char[][] boxes = new char[maxRows][maxCols];
		char[][] goals = new char[maxRows][maxCols];

		for (int i = 0; i < wallsList.size(); i++) {
			for (int j = 0; j < wallsList.get(i).size(); j++) {
				walls[i][j] = wallsList.get(i).get(j);
			}
		}

		for (int i = 0; i < boxesList.size(); i++) {
			for (int j = 0; j < boxesList.get(i).size(); j++) {
				boxes[i][j] = boxesList.get(i).get(j);
			}
		}

		for (int i = 0; i < goalsList.size(); i++) {
			for (int j = 0; j < goalsList.get(i).size(); j++) {
				goals[i][j] = goalsList.get(i).get(j);
			}
		}

		this.initialState.walls = walls;
		this.initialState.boxes = boxes;
		this.initialState.goals = goals;
	}

	public LinkedList<Node> Search() throws IOException, Exception {
		System.err.println("Search starting with strategy.\n");
		Thread.sleep(10000);
		throw new Exception("Not implemeted");
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader serverMessages = new BufferedReader(new InputStreamReader(System.in));
		
		Client level = new Client(serverMessages); 
		
		// Use the search function to find a solution and use it
		LinkedList<Node> solution;
		try 
		{
			solution = level.Search();
		} 
		catch (OutOfMemoryError ex) 
		{
			System.err.println("Maximum memory usage exceeded.");
			solution = null;
		}

		if (solution == null) {
			System.err.println("Unable to solve level.");
			System.exit(0);
		} else {
			System.err.println("\nSummary: ");
			System.err.println("Found solution of length " + solution.size());

			for (Node n : solution) {
				String act = n.action.toString();
				System.out.println(act);
				String response = serverMessages.readLine();
				if (response.contains("false")) {
					System.err.format("Server responsed with %s to the inapplicable action: %s\n", response, act);
					System.err.format("%s was attempted in \n%s\n", act, n.toString());
					break;
				}
			}
		}
	}
}
