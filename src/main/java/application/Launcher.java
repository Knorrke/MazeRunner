package application;

import java.util.logging.Logger;

import application.controller.GameController;
import application.model.Game;
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

    LOG.fine("creating application.model");
    Game game = new Game();

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
    primaryStage.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<javafx.scene.input.KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.P  && event.isControlDown()) {
          System.out.println(game.toString()); 
        }
      }
      
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}
