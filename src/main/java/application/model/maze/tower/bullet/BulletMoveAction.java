package application.model.maze.tower.bullet;

import application.model.baseactions.MoveAction;
import application.model.creature.Creature;

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
