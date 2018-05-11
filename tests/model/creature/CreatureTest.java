package model.creature;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.maze.Maze;
import model.maze.MazeModelInterface;

public class CreatureTest {


	Creature creature;
	MazeModelInterface maze;
	double startX, startY;
	
	@Before
	public void setup() {
		maze = new Maze();
		creature = CreatureFactory.create(maze, CreatureType.NORMAL);
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
	public void shouldMoveForwardOnEmptyMaze() {
		assertEquals("Maze should have no walls", 0, maze.getWalls().size());
		creature.move();
		assertTrue("Creature should move forward (in x direction)", movedTo(creature, startX+1, startY, 0.1));
	}
	
/*	@Test
	public void creatureShouldMoveOutOfBlindAlley() {
		int x = 5, y = 4;
		creature.setX(x);
		creature.setY(y);
		maze.buildWall(x+1, y); //in front (in x)
		maze.buildWall(x, y+1); //above
		maze.buildWall(x, y-1); //below
		
		assertEquals("Maze should have three walls", 3, maze.getWalls().size());
		creature.move();
		assertTrue("Creature should move out of blind alley", movedTo(creature, x-1, y, 0.1));
	}*/
	
	@Test
	public void shouldUpdateVisitedOnMove() {
		int x = 2, y = 3;
		maze.buildWall(x+1, y); //in front (in x)
		maze.buildWall(x, y+1); //above
		maze.buildWall(x, y-1); //below
		creature.move();
		assertTrue("Start field should be visited", creature.getVisitedMap().isVisited((int) startX,(int) startY));
		assertFalse("Start position and new position shouldn't match", (int) startX == (int) creature.getX() && (int) startY == (int) creature.getY());
		assertTrue("Current field should be visited", creature.getVisitedMap().isVisited((int) creature.getX(),(int) creature.getY()));
	}
	
	@Test
	public void shouldUpdateUselessInBlindAlley() {
		int x = (int) startX+1, y = (int) startY;
		maze.buildWall(x+1, y); //2in front (in x)
		maze.buildWall(x, y+1); //above
		maze.buildWall(x, y-1); //below
		creature.move(1,0); //move to x,y
		creature.move();
		assertTrue("BlindAlley should be marked useless", creature.getVisitedMap().isUseless(x, y));
	}
	
	@Test
	public void shouldNotMoveOutOfBoard() {
		int y1 = 5;
		Creature creature1 = CreatureFactory.create(maze, CreatureType.NORMAL, 0, y1 - 1);
		//starting on 0,y1 + 1 move in a circle to visit all fields surrounding 0,y1
		creature1.move(1,0);
		creature1.move(0,1);
		creature1.move(0,1);
		creature1.move(-1,0);
		creature1.move(0,-1);
		
		creature1.move();
		assertTrue("Creature should have moved back", movedTo(creature1, 0, y1+1, 0.1));
	}
	private static boolean isMoved(Creature c, double oldX, double oldY) {
		return !movedTo(c, oldX, oldY, 0.1);
	}
	
	private static boolean movedTo(Creature c, double newX, double newY, double precision) {
		return newX > c.getX()-precision && newX < c.getX() + precision
				&& newY > c.getY() - precision && newY < c.getY() + precision;
	}

}
