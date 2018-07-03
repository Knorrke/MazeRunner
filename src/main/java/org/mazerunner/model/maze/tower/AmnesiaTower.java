package org.mazerunner.model.maze.tower;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.bullet.AmnesiaBullet;
import javafx.util.Duration;

public class AmnesiaTower extends AbstractTower {

  private Set<Creature> shotAt = new HashSet<>();

  protected AmnesiaTower(Wall wall) {
    super(1, 0, 20, 2, TowerType.AMNESIA, wall);
  }

  @Override
  public Runnable createShooter(AbstractTower shooting) {
    return () -> {
      List<Creature> creatures = shooting.findCreaturesInRange();
      if (!creatures.isEmpty()) {
        for (Creature target : creatures) {
          if (!shotAt.contains(target)) {
            shoot(shooting, target);
            return;
          }
        }
        Creature target = creatures.get(new Random().nextInt(creatures.size()));
        shoot(shooting, target);
        return;
      }
    };
  }

  public void shoot(AbstractTower shooting, Creature target) {
    shooting.addBullet(
        new AmnesiaBullet(
            shooting.getX() + 0.5, shooting.getY() + 0.5, Duration.millis(5000), target));
    shotAt.add(target);
  }
}
