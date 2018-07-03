package view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazerunner.model.GameState;
import org.mazerunner.view.popover.GameEndPopOver;
import javafx.stage.Window;

public class GameEndModalIntegrationTest extends AbstractViewTest {

  @Test
  public void getsCreatedWhenGamestateChangesToWin() {
    interact(() -> winGame());
    assertEquals(GameState.WON, game.getState());
    verifyModalVisible();
  }

  @Test
  public void getsCreatedWhenGamestateChangesToLoose() {
    interact(() -> looseGame());
    assertEquals(GameState.GAMEOVER, game.getState());
    verifyModalVisible();
  }

  private void winGame() {
    int numberLevels = level.getCreatureTimeline().size();
    for (int i = 0; i < numberLevels; i++) {
      level.sendNextCreatureWave();
    }
    maze.getCreatures().clear();
  }

  private void looseGame() {
    int numberLifes = player.getLifes();
    for (int i = 0; i < numberLifes; i++) {
      player.looseLife();
    }
  }

  private void verifyModalVisible() {
    boolean visible = false;
    for (Window window : listWindows()) {
      if (window instanceof GameEndPopOver) {
        visible = true;
      }
    }
    assertTrue("Modal is visible", visible);
  }
}
