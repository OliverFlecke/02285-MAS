package lvl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import lvl.cell.Agent;
import lvl.cell.Box;

public class Level {

	public int 						width, 
									height,
									nbAgs;
	public char[][] 				data;
	public Map<Character, String> 	colors;
	public Map<Integer, Agent> 		agents;
	public Set<Box> 				boxes;
	
	private Level(int width, int height, int nbAgs, 
			char[][] data, Map<Character, String> colors) 
	{
		this.width  = width;
		this.height = height;
		this.nbAgs  = nbAgs;
		this.data   = data;
		this.colors = colors;
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
		
		Map<Character, String> 	colors 	= new HashMap<>();
		
		List<List<Character>> dataList = new ArrayList<List<Character>>();
		
		for (String line; (line = msg.readLine()) != null && !line.equals("");)
		{
			if (line.matches("^[a-z]+:\\s*[0-9A-Z](\\s*,\\s*[0-9A-Z])*\\s*$"))
			{
				String color = line.substring(0, line.indexOf(":"));
				char[] chars = line.substring(line.indexOf(" ") + 1)
								   .replace(",", "").toCharArray();
				
				for (int i = 0; i < chars.length; i++) 
				{
					colors.put(chars[i], color);
				}
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
		
		char[][] data = new char[maxCol][maxRow];
		
		for (int row = 0; row < maxRow; row++) 
		{
			for (int col = 0; col < dataList.get(row).size(); col++) 
			{
				data[col][row] = dataList.get(row).get(col);
			}
		}
		return new Level(maxCol, maxRow, nbAgs, data, colors);
	}
}