package mas;

import org.junit.Test;

public class SAEasyTests {

	private static final String PATH = "single_agent\\easy\\";
	
	@Test
	public void SAanagram() { Tester.testLevel("SAanagram"); }
	
	@Test
	public void SAbruteforce() { Tester.testLevel(PATH + "SAbruteforce"); }
	
	@Test
	public void SACrunch() { Tester.testLevel(PATH + "SACrunch"); }
	
	@Test
	public void SADeliRobot() { Tester.testLevel(PATH + "SADeliRobot"); }
	
	@Test
	public void SAFirefly() { Tester.testLevel(PATH + "SAFirefly"); }
	
	@Test
	public void SAgroup42() { Tester.testLevel(PATH + "SAgroup42"); }
	
	@Test
	public void SARyther() { Tester.testLevel(PATH + "SARyther"); }
	
	@Test
	public void SAsampdoria() { Tester.testLevel(PATH + "SAsampdoria"); }
}
