package view;

import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.Bullet;
import application.model.maze.tower.TowerType;

public class BulletViewTest extends AbstractViewTest {
  private AbstractTower tower;

  @Before
  public void setUp() {
    interact(() -> maze.buildWall(2, 3));
    Wall wall = maze.getWalls().get(0);
    tower = AbstractTower.create(TowerType.NORMAL);
    interact(() -> wall.setTower(tower));
    interact(() -> maze.addCreature(CreatureFactory.create(maze, CreatureType.NORMAL, 1, 3)));
  }
  
  @Test
  public void bulletVisibleTest() {
    interact(() -> tower.shoot());
    verifyThat(".bullet", NodeMatchers.isNotNull());
  }
  
  @Test
  public void bulletInvisibleAfterShotTest() {
    interact(() -> tower.shoot());
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
    verifyThat(".bullet", NodeMatchers.isNull());
  }
}
