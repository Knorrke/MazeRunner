package application.model.maze.tower;

import java.util.Arrays;
import java.util.List;
import application.model.creature.Creature;
import application.model.maze.Wall;
import application.model.maze.tower.bullet.DamageBullet;

public class FastTower extends AbstractTower {

  protected FastTower(
      Wall wall) {
    super(4, 1, 15, 2, TowerType.FAST, wall);
    this.getUpgrades()
        .addAll(
            Arrays.asList(
                new TowerUpgrade(rate -> rate+1, damage -> damage, range -> range + 1, 10),
                new TowerUpgrade(rate -> rate+0.5, damage -> damage+1, range -> range + 1, 20),
                new TowerUpgrade(rate -> rate+0.5, damage -> damage+2, range -> range, 30),
                new TowerUpgrade(rate -> rate+1, damage -> damage+2, range -> range, 40)));
  }

  @Override
  public Runnable createShooter(AbstractTower shooting) {
    return () -> {
      List<Creature> creatures = shooting.findCreaturesInRange();
      if (!creatures.isEmpty()) {
        shooting.addBullet(
            new DamageBullet(
                shooting.getX() + 0.5,
                shooting.getY() + 0.5,
                shooting.getDamage(),
                creatures.get(0)));
      }
    };
  }
}
