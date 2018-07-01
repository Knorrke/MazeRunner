package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.controller.GameController;
import application.model.Game;
import application.util.ImageLoader;
import application.util.Serializer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    LOG.fine("creating application.model");
    Game game = savegamePath != null ? createGamefromJsonFile(savegamePath) : new Game();

    LOG.fine("creating gameController");
    GameController gameController = new GameController();

    LOG.fine("initializing Model");
    gameController.initModel(game);
    return gameController;
  }

  public static Scene startScene(GameController gameController, Stage primaryStage) {
    Scene scene = new Scene(gameController.getView());
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
