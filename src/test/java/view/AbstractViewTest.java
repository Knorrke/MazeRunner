package view;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.DebugUtils;

import application.ImageLoader;
import application.Launcher;
import application.controller.GameController;
import application.controller.gameloop.GameLoop;
import application.model.Game;
import application.model.GameModelInterface;
import application.model.level.LevelModelInterface;
import application.model.maze.MazeModelInterface;
import application.model.player.PlayerModelInterface;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class AbstractViewTest extends ApplicationTest {
  protected GameModelInterface game;
  protected MazeModelInterface maze;
  protected LevelModelInterface level;
  protected PlayerModelInterface player;

  protected GameController gameController;
  protected GameLoop gameLoop;

  private Scene scene;

  @Override
  public void init() throws Exception {
    FxToolkit.registerStage(() -> new Stage());
  }

  @Override
  public void start(Stage stage) {
    gameController = Launcher.createGame();
    game = gameController.getModel();
    gameLoop = gameController.getGameLoop();
    maze = game.getMaze();
    level = game.getLevel();
    player = game.getPlayer();
    scene = Launcher.startScene(gameController, stage);
  }

  @Override
  public void stop() throws Exception {
    FxToolkit.hideStage();
  }

  /** Debug helper */
  private final AtomicInteger screenshotNumber = new AtomicInteger(0);

  public Function<StringBuilder, StringBuilder> collectInfos() {
    String dirName = "failed-test-screenshots";
    File dir = new File(dirName);
    if (!dir.isDirectory()) {
      dir.mkdir();
    }
    String testName = this.getClass().getCanonicalName();
    return DebugUtils.compose(
        DebugUtils.insertHeader("Context:"),
        DebugUtils.showKeysPressedAtTestFailure(this),
        DebugUtils.showMouseButtonsPressedAtTestFailure(this),
        DebugUtils.showFiredEvents(),
        DebugUtils.saveWindow(
            scene.getWindow(), dirName + "/" + testName, screenshotNumber.getAndIncrement()));
  }
}
