package application.model.maze.tower;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import application.model.maze.Wall;

public class NoTower extends AbstractTower {

  @JsonCreator
  public NoTower(@JsonProperty("wall") Wall wall) {
    super(0,0,0,0, TowerType.NO, wall);
  }

  @Override
  public void shoot() {}
}
