package application.model;

import static application.model.GameState.BUILDING;

import java.util.List;

import application.model.creature.Creature;
import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import application.model.gameloop.GameLoop;
import application.model.level.Level;
import application.model.level.LevelModelInterface;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.player.Player;
import application.model.player.PlayerModelInterface;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Game implements GameModelInterface {
  private ObjectProperty<GameState> state = new SimpleObjectProperty<GameState>();
  private final PlayerModelInterface player;
  private final LevelModelInterface level;
  private final MazeModelInterface maze;
  private final GameLoop gameloop;

  public Game() {
    player = new Player(50, 20);

    level = new Level(this);
    for (int i = 0; i < 9; i++) {
      level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
    }

    maze = new Maze();
    setState(BUILDING);
    gameloop = new GameLoop(this);
  }

  @Override
  public void update(double dt) {
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

  @Override
  public void start() {
    setState(GameState.RUNNING);
    gameloop.start();
  }

  @Override
  public void pause() {
    setState(GameState.PAUSED);
    gameloop.stop();
  }

  @Override
  public void nextWave(List<Creature> nextCreatures) {
    maze.addAllCreatures(nextCreatures);
  }
}
