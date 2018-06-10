package application.model.maze.tower;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import application.model.creature.Creature;
import application.model.maze.Wall;

public class NormalTower extends AbstractTower {
  @JsonCreator
  public NormalTower(@JsonProperty("wall") Wall wall) {
    super(1, 0, 0, 0, TowerType.NORMAL, wall);
  }

  @Override
  public void shoot() {
    List<Creature> creature = findCreaturesInRange();
  }
}
