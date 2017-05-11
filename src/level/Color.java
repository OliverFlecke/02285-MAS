package level;

public enum Color {
	Blue, 
	Red, 
	Green, 
	Cyan, 
	Magenta, 
	Orange, 
	Pink, 
	Yellow;

	public static Color getColor(String color)
	{
		if (color == null) return Color.Blue;
		
		switch (color)
		{
		case "red":			return Color.Red;
		case "green":		return Color.Green;
		case "cyan":		return Color.Cyan;
		case "magenta":		return Color.Magenta;
		case "orange":		return Color.Orange;
		case "pink": 		return Color.Pink;
		case "yellow":		return Color.Yellow;
		case "blue":
		default: 
			return Color.Blue;
		}
	}
	
	public static Color getColor(int color)
	{
		switch (color)
		{
		case 1:  return Color.Blue;
		case 2:  return Color.Cyan;                           
		case 3:  return Color.Green;                      
		case 4:  return Color.Magenta;                     
		case 5:  return Color.Orange;                   
		case 6:  return Color.Pink;                         
		case 7:  return Color.Red;                            
		case 8:  return Color.Yellow;                           
		default: return Color.Blue;                           
		}
	}
	
	public static int getValue(Color color)
	{
		if (color == null) return 1;
		
		switch (color)
		{
		case Blue: 		return 1;
		case Cyan:     	return 2;                           
		case Green:    	return 3;                           
		case Magenta:  	return 4;                           
		case Orange: 	return 5;                           
		case Pink:		return 6;                           
		case Red:		return 7;                           
		case Yellow: 	return 8;                           
		default: 		return 1;                           
		}
	}
}
