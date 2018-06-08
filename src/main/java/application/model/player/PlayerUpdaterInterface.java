package application.model.player;

import application.model.creature.Creature;
import application.model.maze.Wall;

public interface PlayerUpdaterInterface {
  /**
   * Has to be called, when a creature is Leaving, to update the player lifes
   *
   * @param creature
   */
  public void leavingCreature(Creature creature);

  /**
   * Has to be called, when a new Wall was built, to update the players money
   *
   * @param wall the built wall
   * @return true if player had enough money to build wall.
   */
  public boolean newWallBuilt(Wall wall);

  public void soldWall(Wall wall);
}
