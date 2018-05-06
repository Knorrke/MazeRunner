package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.creature.Creature;
import model.creature.CreatureFactory;
import model.creature.CreatureType;
import model.maze.Maze;

public class CreatureTest {


	Creature creature;
	Maze maze;
	double startX, startY;
	
	@Before
	public void setup() {
		maze = new Maze();
		creature = CreatureFactory.create(CreatureType.NORMAL);
		maze.addCreature(creature);
		startX = creature.getX();
		startY = creature.getY();
	}
	
	@Test
	public void creatureShouldMove() {
		creature.move();
		assertTrue("Creature should move on call of move()", isMoved(creature, startX, startY));	
	}
	
	@Test
	public void creatureShouldMoveForwardOnEmptyMaze() {
		assertEquals("Maze should have no walls", 0, maze.getWalls().size());
		creature.move();
		assertTrue("Creature should move forward (in x direction)", movedTo(creature, startX+1, startY));
	}
	
	
	private static boolean isMoved(Creature c, double oldX, double oldY) {
		return !movedTo(c, oldX, oldY);
	}
	
	private static boolean movedTo(Creature c, double newX, double newY) {
		return newX == c.getX() && newY == c.getY();
	}

}
