package env;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Level {

	int width, height, nbAgs;
	char[][] data;
	Map<String, Set<Integer>> agents;
	Map<String, List<Character>> boxes;
	
	private Level(int width, int height, int nbAgs, char[][] data, 
			Map<String, Set<Integer>> agents, Map<String, List<Character>> boxes) 
	{
		this.width  = width;
		this.height = height;
		this.nbAgs  = nbAgs;
		this.data   = data;
		this.agents = agents;
		this.boxes = boxes;
	}
	
	/**
	 * Parses a message from a BufferedReader, returning a level with
	 * all the relevant information.
	 * @param msg - The message.
	 * @return A new Level object.
	 * @throws IOException
	 */
	public static Level parse(BufferedReader msg) throws IOException
	{
		int maxCol = -1, nbAgs = 0;
		
		Map<String, Set<Integer>> agents = new HashMap<String, Set<Integer>>();
		Map<String, List<Character>> boxes = new HashMap<String, List<Character>>();
		
		List<List<Character>> dataList = new ArrayList<List<Character>>();
		
		for (String line = msg.readLine(); !line.equals(""); line = msg.readLine())
		{
			if (line.matches("^[a-z]+:\\s*[0-9A-Z](\\s*,\\s*[0-9A-Z])*\\s*$"))
			{
				readColor(agents, boxes, line);
			}
			else
			{							
				List<Character> row = new ArrayList<Character>();
				
				for (char ch : line.toCharArray())
				{					
					row.add(ch);
					
					if (Character.isDigit(ch)) nbAgs++;
				}				
				dataList.add(row);
				
				if (row.size() > maxCol) maxCol = row.size();
			}
		}
		int maxRow = dataList.size();
		
		char[][] data = new char[maxRow][maxCol];
		
		for (int row = 0; row < maxRow; row++) 
		{
			for (int col = 0; col < dataList.get(row).size(); col++) 
			{
				data[row][col] = dataList.get(row).get(col);
			}
		}		
		return new Level(maxCol, maxRow, nbAgs, data, agents, boxes);
	}
	
	/**
	 * Read the colors from the current line
	 * @param agents
	 * @param boxes
	 * @param line to read the data from
	 */
	private static void readColor(Map<String, Set<Integer>> agents, 
			Map<String, List<Character>> boxes, String line) 
	{
		int endColorIndex = line.indexOf(":");
		String color = line.substring(0, endColorIndex);
		Set<Integer> currentAgents = new HashSet<Integer>();
		List<Character> currentBoxes = new ArrayList<Character>();
		
		// Read boxes and agents
		for (int i = endColorIndex; i < line.length(); i++)
		{
			char chr = line.charAt(i);
			
			if ('0' <= chr && chr <= '9')
				currentAgents.add(chr - 48);
			else if ('A' <= chr && chr <= 'Z')
				currentBoxes.add(chr);
		}
		
		agents.put(color, currentAgents);
		boxes.put(color, currentBoxes);
	}
}
