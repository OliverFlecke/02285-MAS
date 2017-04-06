package env;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Level {

	int width, height, nbAgs;
	char[][] data;
	HashMap<Character,Integer> colors;
	
	private Level(int width, int height, int nbAgs, char[][] data, 
			HashMap<Character,Integer> colors) 
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
		
		HashMap<Character,Integer> colors = new HashMap<Character,Integer>();
		
		List<List<Character>> dataList = new ArrayList<List<Character>>();
		
		for (String line = msg.readLine(); !line.equals(""); line = msg.readLine())
		{
			if (line.matches("^[a-z]"))
			{
				line = line.substring(line.indexOf(" ") + 1);
				
				// Example of chars: { 0, 1, A, B, C }
				char[] chars = line.replace(",", "").toCharArray();
				
				// Assuming only one agent on each line
				for (int i = 1; i < chars.length; i++) 
				{
					colors.put(chars[i], Character.getNumericValue(chars[0]));
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
		
		char[][] data = new char[maxRow][maxCol];
		
		for (int row = 0; row < maxRow; row++) 
		{
			for (int col = 0; col < dataList.get(row).size(); col++) 
			{
				data[row][col] = dataList.get(row).get(col);
			}
		}		
		return new Level(maxCol, maxRow, nbAgs, data, colors);
	}
}