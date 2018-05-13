package application.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.ImageLoader;
import application.model.GameModelInterface;
import application.model.GameState;
import application.view.maze.MazeView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class GameController implements ModelHolderInterface<GameModelInterface> {
  private static Logger LOG = Logger.getLogger(GameController.class.getName());

  /** Model classes */
  private BooleanProperty paused = new SimpleBooleanProperty(false);

  private GameModelInterface game;

  /** Connected controllers */
  @FXML PlayerController player;

  MazeController mazeController;
  LevelController levelController;

  /** View classes */
  private Parent view;

  @FXML VBox creatureTimelineView;
  @FXML MazeView maze;
  @FXML ImageView pauseButton;

  public GameController() {
    mazeController = new MazeController();
    levelController = new LevelController();

    FXMLLoader gameViewLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("fxml/GameView.fxml"));
    gameViewLoader.setController(this);
    try {
      LOG.fine("loading GameView from fxml");
      view = gameViewLoader.load();
    } catch (IOException exception) {
      LOG.log(Level.SEVERE, "Loading GameView.fxml failed", exception);
      throw new RuntimeException(exception);
    }
  }

  public void initialize() {
    LOG.fine("initializing GameController");
    pauseButton
        .imageProperty()
        .bind(Bindings.when(paused).then(ImageLoader.play).otherwise(ImageLoader.pause));
    levelController.setView(creatureTimelineView);
    mazeController.setView(maze);
  }

  @FXML
  public void togglePause(MouseEvent event) {
    if (paused.get()) {
      game.start();
    } else {
      game.pause();
    }
  }

  public Parent getView() {
    return view;
  }

  @Override
  public void initModel(GameModelInterface game) {
    LOG.finer("initializing GameModel");
    this.game = game;
    player.initModel(game.getPlayer());
    mazeController.initModel(game.getMaze());
    levelController.initModel(game.getLevel());
    paused.bind(game.stateProperty().isNotEqualTo(GameState.RUNNING));
  }
}
