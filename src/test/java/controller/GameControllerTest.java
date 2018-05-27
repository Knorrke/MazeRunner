package controller;

import static org.junit.Assert.*;

import org.junit.Test;

import view.AbstractViewTest;

public class GameControllerTest extends AbstractViewTest {
  @Test
  public void playPauseButtonTest() {
    assertFalse(gameIsRunning());
    clickPlayPause();
    assertTrue(gameIsRunning());
    clickPlayPause();
    assertFalse(gameIsRunning());
  }

  private void clickPlayPause() {
    clickOn("#playPauseButton");
  }

  private boolean gameIsRunning() {
    return gameLoop.runningProperty().get();
  }
}
