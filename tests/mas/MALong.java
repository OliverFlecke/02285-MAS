package mas;

import org.junit.Test;

public class MALong extends Tester {
	
	public static final String PATH = "multi_agent\\long_solution\\";

	@Test public void MAbruteforce() 	{ Tester.testLevel(PATH + "MAbruteforce"); }
	@Test public void MADillema() 		{ Tester.testLevel(PATH + "MADillema"); }
	@Test public void MAEpicPhail() 	{ Tester.testLevel(PATH + "MAEpicPhail"); }
	@Test public void MAStuddyBuddy() 	{ Tester.testLevel(PATH + "MAStuddyBuddy"); }
	@Test public void MAwallE() 		{ Tester.testLevel(PATH + "MAwallE"); }
	
}
