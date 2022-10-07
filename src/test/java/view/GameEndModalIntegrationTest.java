package view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import javafx.stage.Window;
import util.TestFXHelper;
import org.junit.Test;
import org.mazerunner.model.GameState;
import org.mazerunner.view.popover.GameEndPopOver;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

public class GameEndModalIntegrationTest extends AbstractViewTest {

  @Test
  public void notVisibleWhileGamestateIsRunning() {
    verifyModalVisible(false);
    verifyThat(TestFXHelper.carefulQuery("#endModal"), NodeMatchers.isNull(), collectInfos());
  }

  @Test
  public void getsCreatedWhenGamestateChangesToWin() {
    verifyModalVisible(false);
    interact(() -> winGame());
    assertEquals(GameState.WON, game.getState());
    verifyModalVisible(true);
    verifyThat("#gameEndText", LabeledMatchers.hasText("You Win!"));
  }

  @Test
  public void getsCreatedWhenGamestateChangesToLoose() {
    verifyModalVisible(false);
    interact(() -> looseGame());
    assertEquals(GameState.GAMEOVER, game.getState());
    verifyModalVisible(true);
    verifyThat("#gameEndText", LabeledMatchers.hasText("GAME OVER"));
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

  private void verifyModalVisible(boolean visible) {
    verifyThat(
        TestFXHelper.carefulQuery("#endModal"),
        visible ? NodeMatchers.isNotNull() : NodeMatchers.isNull(),
        collectInfos());
  }
}
