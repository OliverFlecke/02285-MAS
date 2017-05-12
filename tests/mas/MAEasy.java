package mas;

import org.junit.Test;

/**
 * All the tests in topfolder, easy, short_solution, and quick_solution 
 */
public class MAEasy {

		private final static String PATH = "multi_agent\\easy\\";
		
		@Test public void MASimple1() 	{ Tester.testLevel("MAsimple1"); }
		@Test public void MASimple2() 	{ Tester.testLevel("MAsimple2"); }
		@Test public void MASimple3() 	{ Tester.testLevel("MAsimple3"); }
		@Test public void MASimple4() 	{ Tester.testLevel("MAsimple4"); }
		@Test public void MASimple5() 	{ Tester.testLevel("MAsimple5"); }
		@Test public void MACrunch() 	{ Tester.testLevel(PATH + "MACrunch", Long.MAX_VALUE); }
		@Test public void MADeliRobot() { Tester.testLevel(PATH + "MADeliRobot"); }
		@Test public void MAFireFly() 	{ Tester.testLevel(PATH + "MAFireFly"); }
		@Test public void MAYSoSirius() { Tester.testLevel(PATH + "MAYSoSirius"); }
		@Test public void MARyther() 	{ Tester.testLevel("multi_agent\\quick_solution\\MARyther"); }
}
