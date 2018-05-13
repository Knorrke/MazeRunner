package model;

import static org.junit.Assert.assertEquals;

import application.model.Game;

import static application.model.GameState.BUILDING;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

  Game game;

  @Before
  public void setup() {
    game = new Game();
  }

  @Test
  public void startState() {
    assertEquals("State should be BUILDING at start", BUILDING, game.getState());
  }
}
