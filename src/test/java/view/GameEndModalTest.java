package view;

import static org.testfx.api.FxAssert.verifyThat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import application.model.GameState;
import application.view.popover.GameEndPopOver;
import javafx.stage.Stage;
import javafx.stage.Window;

@RunWith(Parameterized.class)
public class GameEndModalTest extends ApplicationTest {
  Window window;

  @Parameters(name = "{index}: GameState {0} expects text \"{1}\"")
  public static List<Object[]> data() {
    return Arrays.asList(
        new Object[] {GameState.WON, "You Win!"}, new Object[] {GameState.GAMEOVER, "GAME OVER"});
  }

  @Parameter(0)
  public GameState state;

  @Parameter(1)
  public String expectedText;

  @Override
  public void init() throws Exception {
    FxToolkit.registerStage(() -> new GameEndPopOver(state, new Stage()));
  }

  @After
  public void cleanUp() throws TimeoutException {
    FxToolkit.hideStage();
  }

  @Override
  public void start(Stage stage) throws Exception {
    window = stage;
    stage.show();
  }

  @Test
  public void wonScreenShouldContainYouWin() {
    verifyThat("#gameEndText", LabeledMatchers.hasText(expectedText));
  }
}
