package application.model.maze.tower;

import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import application.model.creature.Creature;
import application.model.maze.Wall;

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
  public Runnable createShooter(AbstractTower shooting) {
    return () -> {
      List<Creature> creatures = shooting.findCreaturesInRange();
      if (!creatures.isEmpty()) {
        shooting.addBullet(
            new Bullet(
                shooting.getX() + 0.5,
                shooting.getY() + 0.5,
                shooting.getDamage(),
                creatures.get(0)));
      }
    };
  }
}
