package env;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelParser {
	
	public final HashMap<Integer,Set<Character>> agentColors;
	public final char[][] level;
	
	public LevelParser(BufferedReader msg) throws IOException 
	{ 
		int maxCol = -1;
		
		agentColors = new HashMap<Integer,Set<Character>>();
		
		List<List<Character>> levelList = new ArrayList<List<Character>>();
		
		for (String line = msg.readLine(); !line.equals(""); line = msg.readLine())
		{
			if (line.matches("^[a-z]"))
			{
				line = line.substring(line.indexOf(" ") + 1);
				
				char[] chars = line.replace(",", "").toCharArray();
				
				Set<Character> colors = new HashSet<Character>();
				
				for (int i = 1; i < chars.length; i++) 
				{
					colors.add(chars[i]);
				}				
				agentColors.put(Character.getNumericValue(chars[0]), colors);				
			}
			else
			{				
				List<Character> row = new ArrayList<Character>();
				
				for (char c : line.toCharArray())
				{
					row.add(c);
				}				
				levelList.add(row);
				
				if (row.size() > maxCol) maxCol = row.size();
			}
		}
		
		level = new char[levelList.size()][maxCol];
		
		for (int i = 0; i < levelList.size(); i++) 
		{
			for (int j = 0; j < levelList.get(i).size(); j++) 
			{
				level[i][j] = levelList.get(i).get(j);
			}
		}		
	}
}
