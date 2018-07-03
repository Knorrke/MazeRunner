package org.mazerunner.model.maze.tower.bullet;

import org.mazerunner.model.baseactions.Action;
import org.mazerunner.model.baseactions.CountdownAction;
import org.mazerunner.model.creature.Creature;
import javafx.util.Duration;

public class SlowdownBullet extends Bullet {
  private Action revertAction;
  private double percentage;

  public SlowdownBullet(
      double sourceX, double sourceY, double percentage, Duration duration, Creature target) {
    this(sourceX, sourceY, percentage, duration, target, 3);
  }

  public SlowdownBullet(
      double sourceX,
      double sourceY,
      double percentage,
      Duration duration,
      Creature target,
      double vel) {
    super(sourceX, sourceY, target, vel);
    this.percentage = percentage;
    this.revertAction =
        new CountdownAction(duration.toMillis()) {
          @Override
          protected void onFinish() {
            double revertSlowdownPercentage = 1 - 1 / (1 - percentage);
            target.slowdown(revertSlowdownPercentage);
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
    getTarget().slowdown(percentage);
  }
}
