import org.junit.Test;

import jason.JasonException;
import jason.infra.centralised.RunCentralisedMAS;

public class Runner {

	@Test
	public void run() {
		try {
			RunCentralisedMAS runner = new RunCentralisedMAS();
			runner.init(new String[] { "test/example.mas2j" });
//			runner.getProject().addSourcePath("src/agent");
			runner.create();
			runner.start();
			runner.waitEnd();
			runner.finish();
		} catch (JasonException e) {
			e.printStackTrace();
		}
	}

}
