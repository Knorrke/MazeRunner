package application.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import application.ImageLoader;
import application.controller.gameloop.GameLoop;
import application.model.GameModelInterface;
import application.util.Serializer;
import application.view.maze.MazeView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class GameController implements ModelHolderInterface<GameModelInterface> {
  private static Logger LOG = Logger.getLogger(GameController.class.getName());

  /** Game time */
  private GameLoop gameloop;

  /** Model classes */
  private GameModelInterface game;

  /** Connected controllers */
  @FXML private PlayerController playerController;

  private MazeController mazeController;
  private LevelController levelController;

  /** View classes */
  private Parent view;

  @FXML private VBox creatureTimelineView;
  @FXML private MazeView maze;
  @FXML private ImageView playPauseButton;

  public GameController() {
    mazeController = new MazeController(this);
    levelController = new LevelController();

    FXMLLoader gameViewLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("fxml/GameView.fxml"));
    gameViewLoader.setController(this);
    try {
      LOG.fine("loading GameView from fxml");
      view = gameViewLoader.load();
      LOG.fine("loading GameView successfull");
    } catch (IOException exception) {
      LOG.log(Level.SEVERE, "Loading GameView.fxml failed", exception);
      throw new RuntimeException(exception);
    }

    addSaveHandler(
        event -> {
          try {
            LOG.info(Serializer.create().writeValueAsString(getModel()));
          } catch (JsonProcessingException e) {
            LOG.log(Level.SEVERE, "Couldn't serialize game", e);
          }
        });
  }

  public void initialize() {
    LOG.fine("initializing GameController");
    levelController.setView(creatureTimelineView);
    mazeController.setView(maze);
  }

  @FXML
  public void togglePlayPause(MouseEvent event) {
    gameloop.togglePlayPause();
  }

  public Parent getView() {
    return view;
  }

  @Override
  public void initModel(GameModelInterface game) {
    LOG.finer("initializing GameModel");
    this.game = game;
    playerController.initModel(this.game.getPlayer());
    mazeController.initModel(this.game.getMaze());
    levelController.initModel(this.game.getLevel());
    gameloop = new GameLoop(this.game);
    playPauseButton
        .imageProperty()
        .bind(
            Bindings.when(gameloop.runningProperty())
                .then(ImageLoader.pause)
                .otherwise(ImageLoader.play));
  }

  public GameLoop getGameLoop() {
    return gameloop;
  }

  public GameModelInterface getModel() {
    return game;
  }

  public void addSaveHandler(EventHandler<KeyEvent> handler) {
    Platform.runLater(
        () -> {
          if (view.getScene() != null) {
            view.getScene()
                .addEventHandler(
                    KeyEvent.KEY_PRESSED,
                    event -> {
                      if (event.getCode() == KeyCode.S && event.isControlDown()) {
                        handler.handle(event);
                      }
                    });
          }
        });
  }
}
