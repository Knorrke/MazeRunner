package application.model.maze.tower.bullet;

import application.model.creature.Creature;
import javafx.util.Duration;

public class SlowdownBullet extends Bullet {
  private int delay;
  private double percentage;
  
  public SlowdownBullet(
      double sourceX, double sourceY, double percentage, Duration duration, Creature target) {
    this(sourceX, sourceY, percentage, duration, target, 2);
  }
  
  public SlowdownBullet(
      double sourceX, double sourceY, double percentage, Duration duration, Creature target, double vel) {
    super(sourceX,sourceY,creature -> creature.slowdown(percentage),target,vel);
    this.percentage = percentage;
    this.delay = (int) duration.toMillis();
  }

  @Override
  public void act(double dt) {
    if (super.hasHitTarget()) {
      delay -= dt;
    }
    super.act(dt);
    if (delay <= 0) {
      double revertSlowdownPercentage = 1 - 1/(1-percentage);
      super.getTarget().slowdown(revertSlowdownPercentage);
    }
  }
  
  @Override
  public boolean isOver() {
    return hasHitTarget() && delay <= 0;
  }
}
