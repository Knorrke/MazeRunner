package org.mazerunner.model;

import static org.mazerunner.model.GameState.BUILDING;
import static org.mazerunner.model.GameState.GAMEOVER;
import static org.mazerunner.model.GameState.RUNNING;
import static org.mazerunner.model.GameState.WON;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.level.Level;
import org.mazerunner.model.level.LevelModelInterface;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.player.Player;
import org.mazerunner.model.player.PlayerModelInterface;

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
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 2; j++) {
        if ((i + j) % 2 == 0) {
          level.addCreatureToTimeline(new CreatureGroup(CreatureType.DUMB, 30));
        }
        level.addCreatureToTimeline(
            new CreatureGroup(CreatureType.NORMAL, 20, 1 + (i * 3 + j) * 0.5));
        level.addCreatureToTimeline(new CreatureGroup(CreatureType.SIGHT, 20, 1 + i));
      }
      level.addCreatureToTimeline(new CreatureGroup(CreatureType.COMMANDER, i + 1, i));
      level.addCreatureToTimeline(new CreatureGroup(CreatureType.TOUGH, 10, 1 + i * 3 * 0.5));
    }
    setState(BUILDING);
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
    player
        .lifesProperty()
        .addListener(
            (obj, oldValue, newValue) -> {
              if (newValue.intValue() <= 0) {
                setState(GAMEOVER);
              }
            });

    Runnable winChecker =
        () -> {
          if (maze.getCreatures().isEmpty()
              && level.getWaveNumber() >= level.getCreatureTimeline().size()) {
            setState(WON);
          }
        };
    maze.getCreatures().addListener((Change<? extends Creature> c) -> winChecker.run());
    level.waveNumberProperty().addListener((obj, oldVal, newVal) -> winChecker.run());
  }

  @Override
  public GameState getState() {
    return state.get();
  }

  @Override
  public ObjectProperty<GameState> stateProperty() {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState(GameState state) {
    this.state.set(state);
  }
}
