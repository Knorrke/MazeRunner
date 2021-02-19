package org.mazerunner.model.maze.tower;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.util.Duration;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.bullet.SlowdownBullet;

public class SlowdownTower extends AbstractTower {

  protected SlowdownTower(Wall wall) {
    super(0.5, 0, 15, 2, TowerType.SLOWDOWN, wall);
    this.getUpgrades()
        .addAll(
            Arrays.asList(
                new TowerUpgrade(rate -> rate, damage -> damage, range -> range, 15),
                new TowerUpgrade(rate -> rate + 0.25, damage -> damage, range -> range, 15),
                new TowerUpgrade(rate -> rate + 0.25, damage -> damage, range -> range, 15),
                new TowerUpgrade(rate -> rate + 0.25, damage -> damage, range -> range, 15)));
  }

  @Override
  public Creature shoot() {
    List<Creature> creatures = findCreaturesInRange();
    Creature target = null;
    if (!creatures.isEmpty()) {
      target = creatures.get(new Random().nextInt(creatures.size()));
      addBullet(
          new SlowdownBullet(
              getX() + 0.5,
              getY() + 0.5,
              getLevel() * 0.05 + 0.15,
              Duration.millis(500 * getLevel() + 2000.0),
              target));
    }
    return target;
  }
}
