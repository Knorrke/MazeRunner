package org.mazerunner.model.maze.tower;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;

public class NoTower extends AbstractTower {

  @JsonCreator
  public NoTower(@JsonProperty("wall") Wall wall) {
    super(0, 0, 0, 0, TowerType.NO, wall);
  }

  @Override
  public Creature shoot() {
    return null;
  }
}
