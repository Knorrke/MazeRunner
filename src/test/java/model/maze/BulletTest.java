package model.maze;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.junit.Test;
import org.mockito.Mockito;

import application.model.creature.Creature;
import application.model.maze.tower.Bullet;

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
    Bullet bullet = new Bullet(bulletStartX, bulletStartY, damage, creature, bulletVel);

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
}
