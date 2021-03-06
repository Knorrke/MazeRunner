package view.maze;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;
import util.TestFXHelper;
import view.AbstractViewTest;

public class BulletViewTest extends AbstractViewTest {
  private AbstractTower tower;
  private Creature creature;

  @Before
  public void setUp() {
    interact(() -> maze.buildWall(2, 3));
    Wall wall = maze.getWalls().get(0);
    tower = AbstractTower.create(TowerType.NORMAL);
    interact(() -> wall.setTower(tower));
    creature = CreatureFactory.create(maze, CreatureType.NORMAL, 1, 3);
    interact(() -> maze.addCreature(creature));
  }

  @Test
  public void bulletVisibleTest() {
    simulateShoot();
    verifyThat(TestFXHelper.carefulQuery(".bullet"), NodeMatchers.isNotNull());
  }

  @Test
  public void bulletInvisibleAfterShotTest() {
    simulateShoot();
    simulateTimeUntilBulletHits();
    verifyThat(TestFXHelper.carefulQuery(".bullet"), NodeMatchers.isNull());
  }

  @Test
  public void creatureDiesAfterEnoughShotsTest() {
    int towerDamage = tower.getDamage();
    int lifesRemainingAfterTwoShots = creature.getLifes() - 2 * towerDamage;
    if (lifesRemainingAfterTwoShots > 0) {
      interact(() -> creature.damage(lifesRemainingAfterTwoShots));
    }
    simulateShoot();
    simulateTimeUntilBulletHits();
    simulateShoot();
    simulateTimeUntilBulletHits();
    assertTrue("Creature should be dead", creature.getLifes() <= 0);
    verifyThat(TestFXHelper.carefulQuery(".creature"), NodeMatchers.isNull());
    // Check if there is a floating label for the won money, that disappears after a second
    verifyThat(TestFXHelper.carefulQuery(".earning-label"), NodeMatchers.isNotNull());
    WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
    verifyThat(TestFXHelper.carefulQuery(".earning-label"), NodeMatchers.isNull());
  }

  public void simulateShoot() {
    interact(() -> tower.shoot());
  }

  public void simulateTimeUntilBulletHits() {
    Bullet bullet = tower.getBullets().get(0);
    interact(
        () -> {
          double dt = 1 / 60.0;
          double timeout = 5;
          while (!bullet.hasHitTarget() && timeout > 0) {
            maze.update(dt);
            timeout -= dt;
          }
          assertTrue("Should not time out", timeout > 0);
          maze.update(dt); // This should clear the bullet now
        });
  }
}
