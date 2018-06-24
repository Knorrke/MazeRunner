package application.model.maze;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import application.controller.gameloop.Updateable;
import application.model.ModelInterface;
import application.model.creature.Creature;
import application.model.maze.tower.TowerType;
import application.model.player.PlayerModelInterface;
import javafx.collections.ObservableList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Maze.class, name = "Maze"),
})
public interface MazeModelInterface extends Updateable, ModelInterface {

  /** @return the walls */
  public ObservableList<Wall> getWalls();

  /**
   * Builds a new wall and returns it at success. If the wall already exists or the player hasn't
   * enough money the wall isn't added and the method returns null
   *
   * @param x
   * @param y
   * @return the wall if successfull
   */
  public Wall buildWall(int x, int y);

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

  /**
   * Returns the wall from the specified position if there is any. Otherwise returns null
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return Matching {@link Wall} object or null, if there's no wall
   */
  public Wall getWallOn(int x, int y);

  /**
   * Checks if there is a creature near the position
   *
   * @param x double
   * @param y double
   */
  public boolean hasCreatureNear(double x, double y);

  /**
   * Returns the creature near the specified position if there is any. Otherwise returns null
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return Matching {@link Wall} object or null, if there's no creature
   */
  public Creature getCreatureNear(double x, double y);

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
    return x >= 0 && x < getMaxWallX() && y >= 0 && y < getMaxWallY();
  }

  /**
   * GameLoop update
   *
   * @param dt
   */
  @Override
  public void update(double dt);

  public void setPlayerModel(PlayerModelInterface player);

  public void sell(Wall wall);

  public void buildTower(Wall wall, TowerType type);

  public void upgradeTower(Wall wall);

  public void creatureDied(Creature creature);
}
