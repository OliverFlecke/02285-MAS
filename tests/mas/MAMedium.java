package mas;

import org.junit.Test;

public class MAMedium extends Tester {

		private static final String PATH 	= "multi_agent\\";
		private static final String SHORT 	= "short_solution\\";
		private static final String MEDIUM 	= "medium\\";
		
		@Test public void MARageQuit() 		{ Tester.testLevel(PATH + SHORT + "MARageQuit"); }
		@Test public void MATeamNoOp() 		{ Tester.testLevel(PATH + SHORT + "MATeamNoOp"); }
		@Test public void FOMAFOAM() 		{ Tester.testLevel(PATH + MEDIUM + "FOMAFOAM"); }
		@Test public void MAAgentSharp()  	{ Tester.testLevel(PATH + MEDIUM + "MAAgentSharp"); }
		@Test public void MADCN()			{ Tester.testLevel(PATH + MEDIUM + "MADCN", 20); }
		@Test public void MAsampdoria()		{ Tester.testLevel(PATH + MEDIUM + "MAsampdoria"); }
}
