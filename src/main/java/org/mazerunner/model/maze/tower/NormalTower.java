package org.mazerunner.model.maze.tower;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.bullet.DamageBullet;

public class NormalTower extends AbstractTower {
  @JsonCreator
  public NormalTower(@JsonProperty("wall") Wall wall) {
    super(1, 3, 5, 2, TowerType.NORMAL, wall);
    this.getUpgrades()
        .addAll(
            Arrays.asList(
                new TowerUpgrade(rate -> rate, damage -> damage + 5, range -> range, 10),
                new TowerUpgrade(rate -> rate, damage -> damage + 15, range -> range + 1, 20),
                new TowerUpgrade(rate -> rate * 1.5, damage -> damage + 5, range -> range, 40),
                new TowerUpgrade(rate -> rate, damage -> damage + 40, range -> range + 2, 100)));
  }

  @Override
  public void shoot() {
    List<Creature> creatures = findCreaturesInRange();
    if (!creatures.isEmpty()) {
      addBullet(new DamageBullet(getX() + 0.5, getY() + 0.5, getDamage(), creatures.get(0)));
    }
  }
}
