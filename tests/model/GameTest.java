package model;

import static model.GameState.BUILDING;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.maze.Wall;

public class GameTest {

	Game game;
	
	@Before
	public void setup() {
		game = new Game();
	}
	
	@Test
	public void startState() {
		assertEquals("State should be BUILDING at start" , BUILDING, game.getState());
	}
	
	@Test
	public void buildTest() {
		ObservableList<Wall> walls = game.getMaze().getWalls();

		int x = 2, y =3;
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
		game.buildWall(x,y);
		assertEquals("Maze should have a wall now", 1, walls.size());
		assertTrue("Listener should have been called", listenerCalled.get());		
	}
	
	@Test
	public void buildWallOntoWallTest() {
		ObservableList<Wall> walls = game.getMaze().getWalls();
		int x=2, y=3;
		assertEquals("Maze should be empty at start", 0, walls.size());
		game.buildWall(x, y);
		assertEquals("Maze should have a wall now", 1, walls.size());
		game.buildWall(x, y);
		assertEquals("Maze should still only have one wall", 1, walls.size());
		game.buildWall(x+1, y+1);
		assertEquals("Maze should have two walls now", 2, walls.size());
	}
}
