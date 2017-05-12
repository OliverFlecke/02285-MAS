package mas;

import org.junit.Test;

public class SAEasy {

	private static final String PATH = "single_agent\\easy\\";
	
		@Test public void SAanagram()		{ Tester.testLevel("SAanagram"); }
		@Test public void SAgroup42() 		{ Tester.testLevel(PATH + "SAgroup42"); }
		@Test public void SARyther() 		{ Tester.testLevel(PATH + "SARyther"); }
		@Test public void SAAgentSharp()  	{ Tester.testLevel("single_agent\\short_solution\\SAAgentSharp"); }
		
		// Short
		@Test public void SALionFish()    	{ Tester.testLevel("single_agent\\short_solution\\SALionFish"); }
		@Test public void SARageQuit()    	{ Tester.testLevel("single_agent\\short_solution\\SARageQuit"); }
		
		// Quick
		@Test public void SAbruteforce() 	{ Tester.testLevel(PATH + "SAbruteforce"); }
		@Test public void SACrunch() 		{ Tester.testLevel(PATH + "SACrunch"); }
		@Test public void SADeliRobot() 	{ Tester.testLevel(PATH + "SADeliRobot"); }
		@Test public void SAFirefly() 		{ Tester.testLevel(PATH + "SAFirefly"); }
		@Test public void SAsampdoria() 	{ Tester.testLevel(PATH + "SAsampdoria"); }

	  @Test public void SAsimple1()       { Tester.testLevel("SAsimple1"); }
    @Test public void SAsimple2()       { Tester.testLevel("SAsimple2"); }
    @Test public void SAtest()          { Tester.testLevel("SAtest"); }
    @Test public void SAtest2()         { Tester.testLevel("SAtest2"); }

}
