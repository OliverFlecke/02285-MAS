package mas;

import org.junit.Test;

public class SADifficult extends Tester {

    private final static String PATH = "single_agent\\difficult\\";

    @Test public void SADillema()       { Tester.testLevel(PATH + "SADillema"); } 
    @Test public void SAHumans()        { Tester.testLevel(PATH + "SAHumans"); } 
    @Test public void SAJasonFour()     { Tester.testLevel(PATH + "SAJasonFour"); }
    @Test public void SAJasonX()        { Tester.testLevel(PATH + "SAJasonX"); } 
    @Test public void SAPHOBAR()        { Tester.testLevel(PATH + "SAPHOBAR"); } 
    @Test public void SAschwenke()      { Tester.testLevel(PATH + "SAschwenke"); } 

    // Slow 
    @Test public void SADinMor()        { Tester.testLevel(PATH + "SADinMor"); } 
    @Test public void SAskynet()        { Tester.testLevel(PATH + "SAskynet"); } 

    // Long
    @Test public void SAWatsOn()        { Tester.testLevel(PATH + "SAWatsOn"); }
    @Test public void SAMASters()       { Tester.testLevel(PATH + "SAMASters"); }
    @Test public void SAEpicPhail()     { Tester.testLevel("single_agent\\long_solution\\SAEpicPhail"); } 
    @Test public void SATalos()         { Tester.testLevel("single_agent\\long_solution\\SATalos"); }

    @Test public void SAbispebjerg()    { Tester.testLevel("SAbispebjerg"); }
    @Test public void SAboxesOfHanoi()  { Tester.testLevel("SAboxesOfHanoi"); }
    @Test public void SAchoice()        { Tester.testLevel("SAchoice"); }
    @Test public void SAFooBar()        { Tester.testLevel("SAFooBar"); }
}