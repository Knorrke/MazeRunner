package view.maze;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.Bullet;
import application.model.maze.tower.TowerType;
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
    verifyThat(".bullet", NodeMatchers.isNotNull());
  }
  
  @Test
  public void bulletInvisibleAfterShotTest() {
    simulateShoot();
    simulateTimeUntilBulletHits();
    verifyThat(".bullet", NodeMatchers.isNull());
  }
  
  @Test
  public void creatureDiesAfterEnoughShotsTest() {
    tower.setDamage(creature.getLifes()/2+1);
    simulateShoot();
    simulateTimeUntilBulletHits();
    simulateShoot();
    simulateTimeUntilBulletHits();
    assertTrue("Creature should be dead", creature.getLifes() < 0);
    verifyThat(".creature", NodeMatchers.isNull());
    //Check if there is a floating label for the won money, that disappears after a second
    verifyThat(".earning-label", NodeMatchers.isNotNull());
    WaitForAsyncUtils.sleep(1000,TimeUnit.MILLISECONDS);
    verifyThat(".earning-label", NodeMatchers.isNull());
  }
  
  
  public void simulateShoot() {
    interact(() -> tower.shoot());
  }
  
  public void simulateTimeUntilBulletHits() {
    Bullet bullet = tower.getBullets().get(0);
    interact(() -> {
      double dt = 1/60.0;
      double timeout = 5;
      while(!bullet.hasHitTarget() && timeout > 0) {
        maze.update(dt);
        timeout -=dt;
      }
      assertTrue("Should not time out", timeout > 0);
      maze.update(dt); //This should clear the bullet now
    });
  }
}
