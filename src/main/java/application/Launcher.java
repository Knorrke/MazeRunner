package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import application.controller.GameController;
import application.model.Game;
import application.util.Serializer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Launcher extends Application {
  private static final Logger LOG = Logger.getLogger(Launcher.class.getName());

  GameController gameController;

  @Override
  public void start(Stage primaryStage) {
    ImageLoader.loadAll();
    Map<String, String> params = this.getParameters().getNamed();

    LOG.fine("creating application.model");
    Game game = params.containsKey("setup")
        ? createGamefromJsonFile(params.get("setup"))
        : new Game();

    LOG.fine("creating gameController");
    gameController = new GameController();

    LOG.fine("initializing Model");
    gameController.initModel(game);
    
    Scene scene = new Scene(gameController.getView());
    LOG.fine("loading stylesheet");
    scene
        .getStylesheets()
        .add(
            getClass()
                .getClassLoader()
                .getResource("stylesheets/application.css")
                .toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.addEventHandler(
        KeyEvent.KEY_PRESSED,
        new EventHandler<javafx.scene.input.KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.P && event.isControlDown()) {
              try {
                System.out.println(Serializer.create().writeValueAsString(game));
              } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
          }
        });
  }

  private Game createGamefromJsonFile(String name) {
    String json = readJson(name);
    System.out.println(json);
    ObjectMapper serializer = Serializer.create();
    if (serializer.canDeserialize(serializer.constructType(Game.class))) {
      try {
        return serializer.readValue(json, Game.class);
      } catch (IOException e) {
        LOG.log(Level.SEVERE, "Failed to convert json to Game", e);
        return null;
      }
    } else {
      LOG.warning("Json doesn't seem to be a Game. Creating a new one.");
      return new Game();
    }
  }

  private String readJson(String name) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("setups/"+name+".json"), "UTF-8"));
      StringBuffer builder = new StringBuffer();
      String nextLine = reader.readLine();
      while(nextLine != null) {
        builder.append(nextLine);
        nextLine = reader.readLine();
      }
      return builder.toString();
    } catch (IOException e) {
      LOG.log(Level.SEVERE, "opening file {} failed", name);
      e.printStackTrace();
      return "";
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
