package mas;

import org.junit.Test;

import jason.JasonException;
import jason.infra.centralised.RunCentralisedMAS;

public class Runner {

	@Test
	public void run() {
		try {
			RunCentralisedMAS.main(new String[] { "test/mas/example.mas2j" });
		} catch (JasonException e) {
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
