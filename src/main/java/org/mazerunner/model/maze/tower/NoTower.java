package org.mazerunner.model.maze.tower;

import org.mazerunner.model.maze.Wall;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NoTower extends AbstractTower {

  @JsonCreator
  public NoTower(@JsonProperty("wall") Wall wall) {
    super(0, 0, 0, 0, TowerType.NO, wall);
  }

  @Override
  public Runnable createShooter(AbstractTower shooting) {
    return () -> {};
  }
}
