package mas;

import org.junit.Test;

public class MADifficult {

    private final static String PATH      = "multi_agent\\";
    private final static String DIFFICULT = "difficult\\";
    private final static String LONG      = "long_solution\\";
    private final static String SLOW      = "slow_solution\\";

    // Slow
    @Test public void MAEpicPhail()       { Tester.testLevel(PATH + SLOW + "MAEpicPhail"); }

    // Long
    @Test public void MAbruteforce()      { Tester.testLevel(PATH + LONG + "MAbruteforce"); }
    @Test public void MADillema()         { Tester.testLevel(PATH + LONG + "MADillema"); }
    @Test public void MAStuddyBuddy()     { Tester.testLevel(PATH + LONG + "MAStuddyBuddy"); }
    @Test public void MAwallE()           { Tester.testLevel(PATH + LONG + "MAwallE"); }

    @Test public void MABullFight()       { Tester.testLevel(PATH + DIFFICULT + "MABullFight"); }
    @Test public void MAGroupXX()         { Tester.testLevel(PATH + DIFFICULT + "MAGroupXX"); }
    @Test public void MAholdkaeft()       { Tester.testLevel(PATH + DIFFICULT + "MAholdkaeft"); }
    @Test public void MAHumans()          { Tester.testLevel(PATH + DIFFICULT + "MAHumans"); }
    @Test public void MAJasonFour()       { Tester.testLevel(PATH + DIFFICULT + "MAJasonFour"); }
    @Test public void MALionFish()        { Tester.testLevel(PATH + DIFFICULT + "MALionFish"); }
    @Test public void MAnull()            { Tester.testLevel(PATH + DIFFICULT + "MAnull"); }
    @Test public void MAParAndriod()      { Tester.testLevel(PATH + DIFFICULT + "MAParAndriod"); }
    @Test public void MAschwenke()        { Tester.testLevel(PATH + DIFFICULT + "MAschwenke"); }

    @Test public void MAbispebjerg()      { Tester.testLevel("MAbispebjerg"); }
    @Test public void MAchallenge()       { Tester.testLevel("MAchallenge"); }
    @Test public void MAFooBar()          { Tester.testLevel("MAFooBar"); }
    @Test public void MAmultiagentSort()  { Tester.testLevel("MAmultiagentSort"); }
    @Test public void MApacman()          { Tester.testLevel("MApacman"); }
    @Test public void MAtbsAppartment()   { Tester.testLevel("MAtbsAppartment"); }
}