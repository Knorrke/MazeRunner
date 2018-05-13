package application.model;

import java.util.List;

import application.model.creature.Creature;
import application.model.gameloop.Updateable;
import application.model.level.LevelModelInterface;
import application.model.maze.MazeModelInterface;
import application.model.player.PlayerModelInterface;
import javafx.beans.property.ObjectProperty;

public interface GameModelInterface extends Updateable, ModelInterface {
  /** @return the player */
  public PlayerModelInterface getPlayer();

  /** @return the level */
  public LevelModelInterface getLevel();

  /** @return the maze */
  public MazeModelInterface getMaze();

  /** @return the state */
  public GameState getState();

  /** start the game */
  public void start();

  /** pause the game */
  void pause();

  ObjectProperty<GameState> stateProperty();

  public void nextWave(List<Creature> nextCreatures);
}
