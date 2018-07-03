package model.maze.tower.bullet;

import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.mazerunner.model.maze.tower.bullet.DamageBullet;
import org.mazerunner.model.maze.tower.bullet.SlowdownBullet;
import org.mockito.Mockito;
import javafx.util.Duration;

public class BulletTest {
  private static Logger LOG = Logger.getLogger(BulletTest.class.getName());

  @Test
  public void movesTowardsCreature() {
    Creature creature = Mockito.mock(Creature.class);

    final double dt = 1 / 60.0;
    AtomicInteger stepsPassed = new AtomicInteger(0);
    double creatureStartX = 2, creatureY = 3;
    Mockito.doAnswer(ctx -> 2 + stepsPassed.get() * dt).when(creature).getX();
    Mockito.doReturn(3.0).when(creature).getY();

    double bulletStartX = 0.5, bulletStartY = 0.5;
    int damage = 5;
    double bulletVel = 3;
    Bullet bullet = new DamageBullet(bulletStartX, bulletStartY, damage, creature, bulletVel);

    // interpolate time till target is hit
    double timeWithOutMovingCreature =
        Point.distance(creatureStartX, creatureY, bulletStartX, bulletStartY) / bulletVel;
    int minSteps = (int) (timeWithOutMovingCreature / dt) - 1;
    int maxSteps =
        (int)
            (Point.distance(
                    creatureStartX + timeWithOutMovingCreature,
                    creatureY,
                    bulletStartX,
                    bulletStartY)
                / dt);

    // simulate time elapsing
    for (; stepsPassed.get() <= maxSteps && !bullet.hasHitTarget(); stepsPassed.getAndIncrement()) {
      bullet.act(dt);
      LOG.fine(
          new StringBuilder()
              .append(
                  String.format(
                      "Step %3d of min %d and max %d: ", stepsPassed.get(), minSteps, maxSteps))
              .append(
                  String.format(
                      "Creature: (%3f,%3f) ; Bullet: (%3f,%3f)",
                      creature.getX(), creature.getY(), bullet.getX(), bullet.getY()))
              .toString());
    }
    assertTrue(stepsPassed.get() > minSteps);
    assertTrue(bullet.hasHitTarget());
  }
  
  @Test
  public void slowdownTest() {
    Creature target = Mockito.mock(Creature.class);
    Bullet bullet = new SlowdownBullet(2, 3, 0.5,Duration.millis(1000), target, 3);
    bullet.hitTarget();
    Mockito.verify(target).slowdown(0.5);
  }
}
