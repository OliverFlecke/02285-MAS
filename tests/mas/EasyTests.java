package mas;

import org.junit.Test;

public class EasyTests {

	private final static String PATH = "multiagent\\easy\\";
	
	@Test
	public void MASimple1() { Tester.testLevel("MAsimple1"); }
	
	@Test 
	public void MASimple2() { Tester.testLevel("MAsimple2"); }
	
	@Test 
	public void MASimple3() { Tester.testLevel("MAsimple3"); }
	
	@Test 
	public void MASimple4() { Tester.testLevel("MAsimple4"); }
	
	@Test 
	public void MASimple5() { Tester.testLevel("MAsimple5"); }
	
	@Test
	public void MACrunch() { Tester.testLevel(PATH + "MACrunch"); }
	
//	@Test 
	public void MADeliRobot() { Tester.testLevel(PATH + "MADeliRobot"); }
	
//	@Test 
	public void MAFireFly() { Tester.testLevel(PATH + "MAFireFly"); }
	
//	@Test 
	public void MAYSoSirius() { Tester.testLevel(PATH + "MAYSoSirius"); }
}
