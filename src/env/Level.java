package env;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import mas.entity.*;

public class Level {

	int width, height, nbAgs;
	char[][] data;
	List<Agent> agents;
	List<Box> boxes;
	
	private Level(int width, int height, int nbAgs, char[][] data, List<Agent> agents, List<Box> boxes) 
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
	public static Level parse(BufferedReader msg) throws IOException, Exception
	{
		int maxCol = -1, nbAgs = 0;
		
		List<Agent> agents = new ArrayList<Agent>();
		List<Box> boxes = new ArrayList<Box>();		
		List<List<Character>> dataList = new ArrayList<List<Character>>();
		
		char rowNumber = 0;
		String line;
		while ((line = msg.readLine()) != null && !line.equals(""))
		{
			if (line.matches("^[a-z]+:\\s*[0-9A-Z](\\s*,\\s*[0-9A-Z])*\\s*$"))
			{
				readColor(agents, boxes, line);
			}
			else
			{					
				List<Character> row = new ArrayList<Character>();
				
				char columNumber = 0;
				for (char ch : line.toCharArray())
				{		
					row.add(ch);
					
					if (Character.isDigit(ch)) 
					{
						nbAgs++;
						Agent agent = agents.stream().filter(a -> a.getId() == (ch - 48)).findFirst().get();
						agent.col = columNumber;
						agent.row = rowNumber;
					}
					else if (Character.isUpperCase(ch))
					{
						Box box = boxes.stream().filter(b -> b.getId() == ch).findFirst().get();
						box.col = columNumber;
						box.row = rowNumber;
					}
					columNumber++;
				}		
				rowNumber++;

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
	 * @throws Exception 
	 */
	private static void readColor(List<Agent> agents, List<Box> boxes, String line) throws Exception 
	{
		int endColorIndex = line.indexOf(":");
		Color color = Color.getColor(line.substring(0, endColorIndex));
		
		// Read boxes and agents
		for (int i = endColorIndex; i < line.length(); i++)
		{
			char chr = line.charAt(i);
			
			if ('0' <= chr && chr <= '9')
				agents.add(new Agent((char) (chr - 48), color));
			else if ('A' <= chr && chr <= 'Z')
				boxes.add(new Box(chr, color));
		}
	}
	
    /**
     * Get the location, color, and character of boxex
     * @return The list of boxes in this level
     */
    public List<Box> getBoxes()
    {
    	return this.boxes;
    }
    
    /**
     * Get the location, color, and id of the agents
     * @return The list of agents in this level
     */
    public List<Agent> getAgents()
    {
    	return this.agents;
    }
}
