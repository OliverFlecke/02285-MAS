package lvl;

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
}
