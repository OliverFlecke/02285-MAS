package mas;

import org.junit.Test;

public class SAMedium extends Tester {

	private final static String PATH = "single_agent\\medium\\";
	
	@Test public void SAAlpha() 	{ Tester.testLevel(PATH + "SAAlpha"); }
    @Test public void SAAteam()		{ Tester.testLevel(PATH + "SAAteam"); }
    @Test public void SABullFight()	{ Tester.testLevel(PATH + "SABullFight"); }
    @Test public void SADCN()		{ Tester.testLevel(PATH + "SADCN"); }
    @Test public void SAFOAM()		{ Tester.testLevel(PATH + "SAFOAM"); } 
    @Test public void SAholdkaeft()	{ Tester.testLevel(PATH + "SAholdkaeft"); }
    @Test public void SAnull()		{ Tester.testLevel(PATH + "SAnull"); } 
    @Test public void SATalos()		{ Tester.testLevel(PATH + "SATalos"); }
    @Test public void SATeamNoOp()	{ Tester.testLevel(PATH + "SATeamNoOp"); }
    @Test public void SAwallE()		{ Tester.testLevel(PATH + "SAwallE"); }
    
}
