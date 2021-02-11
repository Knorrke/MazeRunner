package model.maze.tower.bullet;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.mazerunner.model.maze.tower.bullet.DamageBullet;
import org.mockito.Mockito;

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
