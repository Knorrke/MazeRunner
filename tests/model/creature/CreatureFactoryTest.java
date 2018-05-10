package model.creature;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Test;

import model.creature.Creature;
import model.creature.CreatureFactory;
import model.creature.CreatureGroup;
import model.creature.CreatureType;
import model.maze.Maze;

public class CreatureFactoryTest {

	@Test
	public void createCreatureGroupTest() {
		CreatureType type = CreatureType.NORMAL;
		int number = 20;
		CreatureGroup normalCreatureGroup = new CreatureGroup(type, number);
		List<Creature> creatures = CreatureFactory.createAll(new Maze(), normalCreatureGroup);
		assertEquals("Should be correct length", creatures.size(), number);
		assertTrue("Should be correct type", creatures.stream().allMatch((Creature c) -> c.getType().equals(type)));
		assertNotEquals("Should be different creature objects", creatures.get(0), creatures.get(1));
	}
}
