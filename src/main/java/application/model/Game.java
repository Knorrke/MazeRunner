package application.model;

import static application.model.GameState.BUILDING;
import static application.model.GameState.GAMEOVER;
import static application.model.GameState.RUNNING;
import static application.model.GameState.WON;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import application.model.level.Level;
import application.model.level.LevelModelInterface;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.player.Player;
import application.model.player.PlayerModelInterface;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Game implements GameModelInterface {
  private ObjectProperty<GameState> state = new SimpleObjectProperty<GameState>();

  private PlayerModelInterface player;

  @JsonDeserialize(as = Level.class)
  private LevelModelInterface level;

  @JsonDeserialize(as = Maze.class)
  private MazeModelInterface maze;

  public Game() {
    player = new Player(50, 20);
    maze = new Maze();
    level = new Level();
    connectModels();
    for (int i = 0; i < 3; i++) {
      level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
    }
    setState(BUILDING);
    player.lifesProperty().addListener((obj, oldValue, newValue) -> {
      if (newValue.intValue() <= 0) {
        setState(GAMEOVER);
      }
    });
    BooleanBinding gameFinished = Bindings.createBooleanBinding(
        () -> maze.getCreatures().isEmpty() && level.calculatePassedTimePercentage() == 1,
        maze.getCreatures(), level.passedTimePercentageBinding());
    gameFinished.addListener((obj, oldValue, newValue) -> {
      if (newValue) {
        setState(WON);
      }
    });
  }

  @Override
  public void update(double dt) {
    if (getState().equals(BUILDING)) {
      setState(RUNNING);
    }
    maze.update(dt);
    level.update(dt);
  }

  @Override
  public PlayerModelInterface getPlayer() {
    return player;
  }

  @Override
  public LevelModelInterface getLevel() {
    return level;
  }

  @Override
  public MazeModelInterface getMaze() {
    return maze;
  }

  public void setPlayer(PlayerModelInterface player) {
    this.player = player;
    connectModels();
  }

  public void setMaze(MazeModelInterface maze) {
    this.maze = maze;
    connectModels();
  }

  public void setLevel(LevelModelInterface level) {
    this.level = level;
    connectModels();
  }

  private void connectModels() {
    getMaze().setPlayerModel(getPlayer());
    getLevel().setMazeModel(getMaze());
  }

  @Override
  public GameState getState() {
    return state.get();
  }

  @Override
  public ObjectProperty<GameState> stateProperty() {
    return state;
  }

  /** @param state the state to set */
  public void setState(GameState state) {
    this.state.set(state);
  }
}
