package mas.entity;

public enum Color {
	Blue, 
	Red, 
	Green, 
	Cyan, 
	Magenta, 
	Orange, 
	Pink, 
	Yellow;
	
	public static Color getColor(String color) throws Exception
	{
		switch (color)
		{
		case "red":
			return Color.Red;
		case "blue":
			return Color.Blue;
		case "green":
			return Color.Green;
		case "cyan":
			return Color.Cyan;
		case "magenta":
			return Color.Magenta;
		case "orange":
			return Color.Orange;
		case "pink": 
			return Color.Pink;
		case "yellow":
			return Color.Yellow;
		default: 
			throw new Exception("Unknown color: " + color);
		}
	}
}
