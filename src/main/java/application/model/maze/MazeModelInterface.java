package application.model.maze;

import java.util.List;

import application.controller.MazeController;
import application.model.ModelInterface;
import application.model.creature.Creature;
import application.model.gameloop.Updateable;
import javafx.collections.ObservableList;

public interface MazeModelInterface extends Updateable, ModelInterface {
  /** @return the walls */
  public ObservableList<Wall> getWalls();
  /** @param wall the wall to add */
  public void addWall(Wall wall);

  /**
   * Builds a new wall. If the wall already exists, this is a no-op
   *
   * @param x
   * @param y
   */
  public void buildWall(int x, int y);

  /** @param wall the wall to remove */
  public void removeWall(Wall wall);

  /** @return the creatures */
  public ObservableList<Creature> getCreatures();
  /** @param creature the creature to add */
  public void addCreature(Creature creature);

  /** @param creatures the list of creatures to add */
  public void addAllCreatures(List<Creature> creatures);

  /** @param creature the creature to remove */
  public void removeCreature(Creature creature);

  /**
   * Checks if there is a wall on a certain position
   *
   * @param int x
   * @param int y
   */
  public boolean hasWallOn(int x, int y);

  /** @return the maximal X value for walls */
  public int getMaxWallX();

  /** @return the maximal Y value for walls */
  public int getMaxWallY();

  /**
   * Check if the coordinates are valid
   *
   * @param x
   * @param y
   * @return true if and only if x >= 0 && x < maxWallX && y >= 0 && y <= maxWallY
   */
  public default boolean checkBounds(int x, int y) {
    return x >= 0 && x < getMaxWallX() && y >= 0 && y <= getMaxWallY();
  }

  /**
   * GameLoop update
   *
   * @param dt
   */
  public void update(double dt);
}
