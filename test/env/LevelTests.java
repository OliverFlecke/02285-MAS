package env;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import mas.entity.*;

public class LevelTests {

	@Test
	public void parseLevelTest() {		
		try (BufferedReader reader = new BufferedReader(new FileReader(new File("levels\\multi_agent\\easy\\MACrunch.lvl")))) 
		{
			Level level = Level.parse(reader);
			
			Agent agent = level.agents.stream().filter(a -> a.getId() == 0).findFirst().get();
			assertNotNull(agent);
			assertEquals(Color.Red, agent.color);
			assertEquals(7, agent.col);
			assertEquals(2, agent.row);
			
			agent = level.agents.stream().filter(a -> a.getId() == 1).findFirst().get();
			assertNotNull(agent);
			assertEquals(Color.Green, agent.color);
			assertEquals(11, agent.col);
			assertEquals(2, agent.row);
			
			Box box = level.boxes.stream().filter(b -> b.getId() == 'A').findFirst().get();
			assertNotNull(box);
			assertEquals(Color.Green, box.color);
			assertEquals(17, box.col);
			assertEquals(1, box.row);
			
			box = level.boxes.stream().filter(b -> b.getId() == 'B').findFirst().get();
			assertNotNull(box);
			assertEquals(Color.Red, box.color);
			assertEquals(1, box.col);
			assertEquals(1, box.row);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			fail("exception");
		}
	}

}
