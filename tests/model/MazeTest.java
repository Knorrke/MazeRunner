package model;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.maze.Maze;
import model.maze.MazeModelInterface;
import model.maze.Wall;

public class MazeTest {
	MazeModelInterface maze;
	int x,y;
	
	@Before
	public void setup() {
		maze = new Maze();
		x = 2;
		y = 3;
	}
	
	@Test
	public void testHasWall() {
		assertFalse("Shouldn't have a wall yet", maze.hasWallOn(x,y));
		maze.addWall(new Wall(x,y));
		assertTrue("Should have wall", maze.hasWallOn(x,y));
	}
	
	@Test
	public void buildTest() {
		ObservableList<Wall> walls = maze.getWalls();

		AtomicBoolean listenerCalled = new AtomicBoolean(false);
		walls.addListener((ListChangeListener<Wall>) c -> {
			assertFalse("listener should not have been called before", listenerCalled.getAndSet(true));
			while(c.next()) {
				assertTrue("Element should be added", c.wasAdded());
				assertEquals("Should be one element added", 1, c.getAddedSize());
				assertEquals("Position should be as build", x, c.getAddedSubList().get(0).getX());
				assertEquals("Position should be as build", y, c.getAddedSubList().get(0).getY());
			}
		});
		

		assertEquals("Maze should be empty at start", 0, walls.size());
		maze.buildWall(x,y);
		assertEquals("Maze should have a wall now", 1, walls.size());
		assertTrue("Listener should have been called", listenerCalled.get());		
	}
	
	@Test
	public void buildWallOntoWallTest() {
		ObservableList<Wall> walls = maze.getWalls();
		assertEquals("Maze should be empty at start", 0, walls.size());
		maze.buildWall(x, y);
		assertEquals("Maze should have a wall now", 1, walls.size());
		maze.buildWall(x, y);
		assertEquals("Maze should still only have one wall", 1, walls.size());
		maze.buildWall(x+1, y+1);
		assertEquals("Maze should have two walls now", 2, walls.size());
	}

}
