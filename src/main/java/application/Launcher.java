package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
  private static Logger LOG = Logger.getLogger(Launcher.class.getName());

  GameController gameController;

  @Override
  public void start(Stage primaryStage) {
    ImageLoader.loadAll();
    Map<String, String> params = this.getParameters().getNamed();

    LOG.fine("creating application.model");
    Game game = params.containsKey("setup")
        ? Serializer.create().fromJson(readJson(params.get("setup")), Game.class)
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
              System.out.println(Serializer.create().toJson(game));
            }
          }
        });
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
