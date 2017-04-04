package mas;

import jason.JasonException;
import jason.infra.centralised.RunCentralisedMAS;

public class SearchClient {
	
	public static void main(String[] args) {
		
//		System.out.println("[Move(N)]");
		
		if (args.length == 0) 
		{
			args = new String[] { "src/mas/example.mas2j" };
		}
		
		System.out.println("THis is a change");
		try {
			RunCentralisedMAS.main(args);
		} 
		catch (JasonException e) 
		{
			e.printStackTrace();
		}

		try {
			// Prevent instant termination in case of exception
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
