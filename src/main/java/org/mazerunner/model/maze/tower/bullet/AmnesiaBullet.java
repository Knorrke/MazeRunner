package org.mazerunner.model.maze.tower.bullet;

import org.mazerunner.model.baseactions.Action;
import org.mazerunner.model.baseactions.CountdownAction;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.RandomMovement;
import javafx.util.Duration;

public class AmnesiaBullet extends Bullet {
  private Action revertAction;

  public AmnesiaBullet(double sourceX, double sourceY, Duration duration, Creature target) {
    this(sourceX, sourceY, duration, target, 3);
  }

  public AmnesiaBullet(
      double sourceX, double sourceY, Duration duration, Creature target, double vel) {
    super(sourceX, sourceY, target, vel);
    MovementInterface saved = target.getMovementStrategy();
    this.revertAction =
        new CountdownAction(duration.toMillis()) {
          @Override
          protected void onFinish() {
            target.setMovementStrategy(saved);
          }
        };
  }

  @Override
  public void act(double dt) {
    if (super.hasHitTarget()) {
      revertAction.act(dt);
    } else {
      super.act(dt);
    }
  }

  @Override
  public boolean isOver() {
    return hasHitTarget() && revertAction.isFinished();
  }

  @Override
  public void hitTarget() {
    super.hitTarget();
    getTarget().setMovementStrategy(new RandomMovement());
  }
}
