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
                new TowerUpgrade(getFireRate(), 5, 10, getVisualRange()),
                new TowerUpgrade(getFireRate(), 15, 20, getVisualRange() + 3),
                new TowerUpgrade(getFireRate() * 2, 5, 40, getVisualRange()),
                new TowerUpgrade(getFireRate(), 40, 100, getVisualRange() + 3)));
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
