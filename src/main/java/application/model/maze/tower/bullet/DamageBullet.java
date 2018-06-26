package application.model.maze.tower.bullet;

import application.model.creature.Creature;

public class DamageBullet extends Bullet {

  public DamageBullet(double sourceX, double sourceY, int damage, Creature target) {
    this(sourceX, sourceY,damage, target, 3);
  }
  
  public DamageBullet(double sourceX, double sourceY, int damage, Creature target, double vel) {
    super(sourceX,sourceY,creature -> creature.damage(damage),target, vel);
  }
}
