package org.mazerunner.model.maze.tower;

import java.util.Arrays;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.bullet.DamageBullet;

public class FastTower extends AbstractTower {

  protected FastTower(Wall wall) {
    super(4, 1, 15, 2, TowerType.FAST, wall);
    this.getUpgrades()
        .addAll(
            Arrays.asList(
                new TowerUpgrade(rate -> rate + 1, damage -> damage, range -> range + 1, 10),
                new TowerUpgrade(rate -> rate + 0.5, damage -> damage + 1, range -> range + 1, 20),
                new TowerUpgrade(rate -> rate + 0.5, damage -> damage + 2, range -> range, 30),
                new TowerUpgrade(rate -> rate + 1, damage -> damage + 2, range -> range, 40)));
  }

  @Override
  public void shoot() {
    Creature target = target();
    if (target != null) {
      addBullet(new DamageBullet(getX() + 0.5, getY() + 0.5, getDamage(), target));
    }
  }
}
