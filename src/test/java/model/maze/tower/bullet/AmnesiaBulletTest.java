package model.maze.tower.bullet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import application.model.creature.Creature;
import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.RandomMovement;
import application.model.maze.tower.bullet.AmnesiaBullet;
import application.model.maze.tower.bullet.Bullet;
import javafx.util.Duration;

public class AmnesiaBulletTest {

  @Test
  public void amnesiaTest() {
    Creature target = Mockito.mock(Creature.class);
    MovementInterface movement = Mockito.mock(MovementInterface.class);
    Mockito.when(target.getMovementStrategy()).thenReturn(movement);
    Bullet bullet = new AmnesiaBullet(2, 3, Duration.millis(2500), target);
    bullet.hitTarget();
    Mockito.verify(target).setMovementStrategy(ArgumentMatchers.any(RandomMovement.class));
    assertTrue(bullet.hasHitTarget());
    assertFalse(bullet.isOver());
    bullet.act(2000);
    assertFalse(bullet.isOver());
    bullet.act(500);
    assertTrue(bullet.isOver());
    Mockito.verify(target).setMovementStrategy(movement);
  }
}
