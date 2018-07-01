package application.model.maze.tower.bullet;

import application.model.creature.Creature;

public class DamageBullet extends Bullet {

  private int damage;
  
  public DamageBullet(double sourceX, double sourceY, int damage, Creature target) {
    this(sourceX, sourceY,damage, target, 3);
  }
  
  public DamageBullet(double sourceX, double sourceY, int damage, Creature target, double vel) {
    super(sourceX,sourceY,target, vel);
    this.damage = damage;
  }
  
  @Override
  public void hitTarget() {
    super.hitTarget();
    getTarget().damage(damage);
  }
}
