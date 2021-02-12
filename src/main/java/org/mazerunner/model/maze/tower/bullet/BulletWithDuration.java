package org.mazerunner.model.maze.tower.bullet;

import org.mazerunner.model.baseactions.Action;
import org.mazerunner.model.creature.Creature;

public abstract class BulletWithDuration extends Bullet {
  protected Action revertAction;

  public BulletWithDuration(
      double sourceX, double sourceY, Creature target, double vel, Action revertAction) {
    super(sourceX, sourceY, target, vel);
    this.revertAction = revertAction;
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
}
