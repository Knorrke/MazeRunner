package model;

import static org.junit.Assert.*;

import org.junit.Test;

import model.maze.Maze;
import model.maze.Wall;

public class MazeTest {

	@Test
	public void testHasWall() {
		Maze maze = new Maze();
		int x=2, y=3;
		assertFalse("Shouldn't have a wall yet", maze.hasWallOn(x,y));
		maze.addWall(new Wall(x,y));
		assertTrue("Should have wall", maze.hasWallOn(x,y));
	}

}
