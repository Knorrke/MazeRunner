package org.mazerunner.model.maze.tower;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.bullet.SlowdownBullet;
import javafx.util.Duration;

public class SlowdownTower extends AbstractTower {

  protected SlowdownTower(
      Wall wall) {
    super(0.5, 0, 15, 2, TowerType.SLOWDOWN, wall);
    this.getUpgrades()
        .addAll(
            Arrays.asList(
                new TowerUpgrade(rate -> rate, damage -> damage, range -> range, 15),
                new TowerUpgrade(rate -> rate+0.25, damage -> damage, range -> range, 15),
                new TowerUpgrade(rate -> rate+0.25, damage -> damage, range -> range, 15),
                new TowerUpgrade(rate -> rate+0.25, damage -> damage, range -> range, 15)));
  }

  @Override
  public Runnable createShooter(AbstractTower shooting) {
    return () -> {
      List<Creature> creatures = shooting.findCreaturesInRange();
      if (!creatures.isEmpty()) {
        Creature target = creatures.get(new Random().nextInt(creatures.size()));
        shooting.addBullet(
            new SlowdownBullet(
                shooting.getX() + 0.5,
                shooting.getY() + 0.5,
                shooting.getLevel() * 0.05 + 0.15,
                Duration.millis(500 * shooting.getLevel() + 2000),
                target));
      }
    };
  }
}
