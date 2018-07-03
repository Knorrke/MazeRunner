package org.mazerunner.model.maze.tower.bullet;

import org.mazerunner.model.baseactions.MoveAction;
import org.mazerunner.model.creature.Creature;

public class BulletMoveAction extends MoveAction {
  private Bullet bullet;
  
  public BulletMoveAction(Bullet bullet, Creature target) {
    super(bullet, target);
    this.bullet = bullet;
  }
  
  @Override
  protected void onFinish() {
    super.onFinish();
    bullet.hitTarget();
  }
}
