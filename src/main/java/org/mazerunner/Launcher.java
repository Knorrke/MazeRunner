package org.mazerunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.PlatformUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.mazerunner.controller.GameController;
import org.mazerunner.model.Game;
import org.mazerunner.util.ImageLoader;
import org.mazerunner.util.ScaleUtil;
import org.mazerunner.util.Serializer;

public class Launcher extends Application {
  private static final Logger LOG = Logger.getLogger(Launcher.class.getName());

  private GameController gameController;

  @Override
  public void start(Stage primaryStage) {
    Map<String, String> params = this.getParameters().getNamed();
    if (params.containsKey("setup")) {
      gameController = createGame("setups/" + params.get("setup") + ".json");
    } else {
      gameController = createGame();
    }
    startScene(gameController, primaryStage);
  }

  public static GameController createGame() {
    return createGame(null);
  }

  public static GameController createGame(String savegamePath) {
    ImageLoader.loadAll();
    LOG.fine("creating org.mazerunner.model");
    Game game = savegamePath != null ? createGamefromJsonFile(savegamePath) : new Game();

    LOG.fine("creating gameController");
    GameController gameController = new GameController();

    LOG.fine("initializing Model");
    gameController.initModel(game);
    return gameController;
  }

  public static Scene startScene(GameController gameController, Stage primaryStage) {
    Parent view = gameController.getView();
    int prefWidth = 800, prefHeight = 500;
    Pane pane = new Pane(view);

    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double width = isMobile() ? bounds.getWidth() : prefWidth;
    double height = isMobile() ? bounds.getHeight() : prefHeight;
    Scene scene = new Scene(pane, width, height);

    Scale scale = ScaleUtil.getScale();
    scale.xProperty().bind(scene.widthProperty().divide(prefWidth));
    scale.yProperty().bind(scene.heightProperty().divide(prefHeight));
    view.getTransforms().add(scale);

    LOG.fine("loading stylesheet");
    scene
        .getStylesheets()
        .add(
            Launcher.class
                .getClassLoader()
                .getResource("stylesheets/application.css")
                .toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.show();
    return scene;
  }

  private static boolean isMobile() {
    return PlatformUtil.isAndroid() || PlatformUtil.isIOS();
  }

  private static Game createGamefromJsonFile(String path) {
    String json = readJson(path);
    LOG.log(Level.FINE, "Trying to deserialize {}", json);
    ObjectMapper serializer = Serializer.create();
    try {
      return serializer.readValue(json, Game.class);
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "Failed to convert json to Game", e);
      return null;
    }
  }

  private static String readJson(String path) {
    try {
      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(
                  Launcher.class.getClassLoader().getResourceAsStream(path), "UTF-8"));
      StringBuffer builder = new StringBuffer();
      String nextLine = reader.readLine();
      while (nextLine != null) {
        builder.append(nextLine);
        nextLine = reader.readLine();
      }
      return builder.toString();
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "opening file {} failed", path);
      e.printStackTrace();
      return "";
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
