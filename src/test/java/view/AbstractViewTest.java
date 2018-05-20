package view;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.DebugUtils;

import application.ImageLoader;
import application.controller.GameController;
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

  private Scene scene;

  @BeforeClass
  public static void setupSpec() throws Exception {
    if (Boolean.getBoolean("headless")) {
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.text", "t2k");
      System.setProperty("java.awt.headless", "true");
    }
  }

  @Override
  public void init() throws Exception {
    FxToolkit.registerStage(() -> new Stage());
  }

  @Override
  public void start(Stage stage) {
    ImageLoader.loadAll();
    game = new Game();
    GameController gameController = new GameController();
    gameController.initModel(game);
    scene = new Scene(gameController.getView());
    scene
        .getStylesheets()
        .add(
            getClass()
                .getClassLoader()
                .getResource("stylesheets/application.css")
                .toExternalForm());
    maze = game.getMaze();
    level = game.getLevel();
    player = game.getPlayer();
    stage.setScene(scene);
    stage.show();
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
