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
}
