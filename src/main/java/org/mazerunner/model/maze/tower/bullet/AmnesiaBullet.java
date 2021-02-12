package org.mazerunner.model.maze.tower.bullet;

import javafx.util.Duration;
import org.mazerunner.model.baseactions.CountdownAction;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.RandomMovement;

public class AmnesiaBullet extends BulletWithDuration {
  public AmnesiaBullet(double sourceX, double sourceY, Duration duration, Creature target) {
    this(sourceX, sourceY, duration, target, 3);
  }

  public AmnesiaBullet(
      double sourceX, double sourceY, Duration duration, Creature target, double vel) {
    super(
        sourceX,
        sourceY,
        target,
        vel,
        new CountdownAction(duration.toMillis()) {
          MovementInterface saved = target.getMovementStrategy();

          @Override
          protected void onFinish() {
            target.setMovementStrategy(saved);
          }
        });
  }

  @Override
  public void hitTarget() {
    super.hitTarget();
    getTarget().setMovementStrategy(new RandomMovement());
  }
}
