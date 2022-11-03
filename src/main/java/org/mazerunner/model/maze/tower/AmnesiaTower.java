package org.mazerunner.model.maze.tower;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.util.Duration;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.bullet.AmnesiaBullet;

public class AmnesiaTower extends AbstractTower {

  private Set<Creature> shotAt = new HashSet<>();

  protected AmnesiaTower(Wall wall) {
    super(1, 0, 20, 2, TowerType.AMNESIA, wall);
  }

  @Override
  protected Creature target() {
    List<Creature> creatures = findCreaturesInRange();
    if (creatures.isEmpty()) {
      return null;
    }
    for (Creature target : creatures) {
      if (!shotAt.contains(target)) {
        return target;
      }
    }
    return creatures.get(new Random().nextInt(creatures.size()));
  }

  @Override
  public void shoot() {
    Creature target = target();
    if (target == null) return;
    addBullet(new AmnesiaBullet(getX() + 0.5, getY() + 0.5, Duration.millis(5000), target));
    shotAt.add(target);
  }
}
