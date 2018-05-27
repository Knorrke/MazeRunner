package controller;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testfx.util.WaitForAsyncUtils;

import application.controller.gameloop.GameLoop;
import application.controller.gameloop.Updateable;
import view.AbstractViewTest;

public class GameLoopTest extends AbstractViewTest {
  @Test
  public void updateableCalledTest() {
    Updateable updateable = Mockito.mock(Updateable.class);
    //GameLoop extends AnimationTimer, so this needs to be in FX-Runtime (so it needs to extend AbstractViewTest)
    GameLoop localeGameloop = new GameLoop(updateable);
    localeGameloop.start();
    WaitForAsyncUtils.sleep(100, TimeUnit.MILLISECONDS);
    Mockito.verify(updateable, Mockito.atLeastOnce()).update(ArgumentMatchers.anyDouble());
  }

  @Test
  public void togglePauseTest() {
    assertFalse(isRunning());
    interact(() -> gameLoop.togglePlayPause());
    assertTrue(isRunning());
    interact(() -> gameLoop.togglePlayPause());
    assertFalse(isRunning());
  }

  @Test
  public void startStopTest() {
    assertFalse("shouldn't be running at the beginning", isRunning());
    interact(() -> gameLoop.start());
    assertTrue("should be running after start", isRunning());
    interact(() -> gameLoop.start());
    assertTrue("should still be running after start", isRunning());
    interact(() -> gameLoop.stop());
    assertFalse("should stop after stop()", isRunning());
    interact(() -> gameLoop.stop());
    assertFalse("should still be stopped after second stop()", isRunning());
  }

  private boolean isRunning() {
    return gameLoop.runningProperty().get();
  }
}
