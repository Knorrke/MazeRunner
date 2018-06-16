package application.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import application.controller.gameloop.GameLoop;
import application.model.GameModelInterface;
import application.util.FXMLLoaderUtil;
import application.util.ImageLoader;
import application.util.Serializer;
import application.view.GameEndModal;
import application.view.level.CreatureTimelineView;
import application.view.maze.MazeView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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

  @FXML private CreatureTimelineView creatureTimelineView;
  @FXML private MazeView maze;
  @FXML private ImageView playPauseButton;

  public GameController() {
    mazeController = new MazeController(this);
    levelController = new LevelController();
    view = FXMLLoaderUtil.load("GameView.fxml", this);
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
    game.stateProperty()
        .addListener(
            (obj, oldValue, newValue) -> {
              switch (newValue) {
                case GAMEOVER:
                case WON:
                  mazeController.getView().addEventFilter(EventType.ROOT, event -> event.consume());
                  new GameEndModal(newValue, view.getScene().getWindow());
                case RUNNING:
                case BUILDING:
                default:
                  return;
              }
            });
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
