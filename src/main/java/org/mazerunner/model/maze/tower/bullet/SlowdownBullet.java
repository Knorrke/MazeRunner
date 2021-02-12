package org.mazerunner.model.maze.tower.bullet;

import javafx.util.Duration;
import org.mazerunner.model.baseactions.CountdownAction;
import org.mazerunner.model.creature.Creature;

public class SlowdownBullet extends BulletWithDuration {
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
    super(
        sourceX,
        sourceY,
        target,
        vel,
        new CountdownAction(duration.toMillis()) {
          @Override
          protected void onFinish() {
            double revertSlowdownPercentage = 1 - 1 / (1 - percentage);
            target.slowdown(revertSlowdownPercentage);
          }
        });
    this.percentage = percentage;
  }

  @Override
  public void hitTarget() {
    super.hitTarget();
    getTarget().slowdown(percentage);
  }
}
