package model.maze.tower.bullet;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Mockito;
import application.model.creature.Creature;
import application.model.maze.tower.bullet.Bullet;
import application.model.maze.tower.bullet.DamageBullet;

public class DamageBulletTest {
  @Test
  public void damageTest() {
    Creature target = Mockito.mock(Creature.class);
    Bullet bullet = new DamageBullet(2, 3, 20, target);
    bullet.hitTarget();
    Mockito.verify(target).damage(20);
    assertTrue(bullet.hasHitTarget());
    assertTrue(bullet.isOver());
  }
}
