package model.maze.tower.bullet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.mazerunner.model.maze.tower.bullet.SlowdownBullet;
import org.mockito.Mockito;
import javafx.util.Duration;

public class SlowdownBulletTest {
  
  @Test
  public void slowdownTest() {
    Creature target = Mockito.mock(Creature.class);
    Bullet bullet = new SlowdownBullet(2, 3, 0.5, Duration.millis(2500), target);
    bullet.hitTarget();
    Mockito.verify(target).slowdown(0.5);
    assertTrue(bullet.hasHitTarget());
    assertFalse(bullet.isOver());
    bullet.act(2000);
    assertFalse(bullet.isOver());
    bullet.act(500);
    assertTrue(bullet.isOver());
  }
}
