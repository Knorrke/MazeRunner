package model.maze.tower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.collections.FXCollections;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@RunWith(Enclosed.class)
public class TowerTest {

  public static class NotParameterizedTests {
    private AbstractTower tower;

    @Test
    public void setupTest() {
      tower = AbstractTower.create(TowerType.NO);
      assertNotNull("AbstractTower should not be null", tower);
      assertEquals("Fire rate should be 0", 0, tower.getFireRate(), 0);
    }

    @Test
    public void fireRateTest() {
      AtomicBoolean called = new AtomicBoolean(false);
      double fireRate = 5;
      tower =
          new AbstractTower(fireRate, 0, 0, 0, TowerType.NO, null) {
            @Override
            protected Creature target() {
              return null;
            }

            @Override
            public void shoot() {
              called.set(true);
            }
          };
      double delayBetweenShots = 1 / fireRate;
      tower.act(delayBetweenShots / 2);
      assertFalse("Shoot should not be called yet", called.getAndSet(false));
      tower.act(delayBetweenShots);
      assertTrue("Shoot should be called now", called.getAndSet(false));
    }

    @Test
    public void normalTowerTest() {
      Wall wall = Mockito.mock(Wall.class);
      tower = AbstractTower.create(wall, TowerType.NORMAL);
      double fireRate = tower.getFireRate();
      assertTrue(fireRate > 0);
      double delayBetweenShots = 1 / fireRate;
      tower.act(delayBetweenShots);
      Mockito.verify(wall, Mockito.atLeastOnce())
          .getCreaturesMatchingCondition(ArgumentMatchers.any());
    }

    @Test
    public void findCreaturesInRange() {
      int wallX = 2, wallY = 3;
      tower = AbstractTower.create(TowerType.NORMAL);
      double visualRange = tower.getVisualRange();
      Wall wall = new Wall(wallX, wallY, tower);

      MazeModelInterface mazeMock = Mockito.mock(Maze.class);
      Creature creatureInRange =
          CreatureFactory.create(mazeMock, CreatureType.NORMAL, wallX + visualRange / 2, wallY);
      Creature creatureNotInRange =
          CreatureFactory.create(mazeMock, CreatureType.NORMAL, wallX + visualRange + 1, wallY);
      Mockito.doReturn(FXCollections.observableArrayList(creatureInRange, creatureNotInRange))
          .when(mazeMock)
          .getCreatures();
      wall.setMaze(mazeMock);

      List<Creature> creaturesInRange = tower.findCreaturesInRange();
      assertEquals("one creature in range", 1, creaturesInRange.size());
      assertSame("should be the expectedCreature", creatureInRange, creaturesInRange.get(0));
    }

    @Test
    public void upgradeNormalTower() {
      tower = AbstractTower.create(TowerType.NORMAL);
      int costsBefore = tower.getCosts();
      int damage = tower.getDamage();
      TowerType type = tower.getType();
      assertEquals(0, tower.getLevel());

      tower = tower.upgrade();
      assertNotEquals(costsBefore, tower.getCosts());
      assertNotEquals(damage, tower.getDamage());
      assertEquals(type, tower.getType());
      assertEquals(1, tower.getLevel());
    }
  }

  @RunWith(Parameterized.class)
  public static class UpgradesTests {

    @Parameters(name = "{index}: Shoot test with tower at level {0}")
    public static Object[][] data() {
      return new Object[][] {{0}, {1}, {2}, {3}};
    }

    @Parameter(0)
    public int level;

    private AbstractTower tower;

    @Before
    public void initialize() {
      tower = AbstractTower.create(TowerType.NORMAL);
      for (int i = 0; i < level; i++) {
        tower = tower.upgrade();
      }
    }

    @Test
    public void shootCreatureInRange() {
      assertEquals(level, tower.getLevel());
      int wallX = 2, wallY = 3;
      double visualRange = tower.getVisualRange();
      Wall wall = new Wall(wallX, wallY, tower);

      MazeModelInterface mazeMock = Mockito.mock(Maze.class);
      Creature creatureInRange = mockCreature(wallX + visualRange - 0.1, wallY);
      Creature creatureNotInRange = mockCreature(wallX + visualRange + 1, wallY);
      Mockito.doReturn(FXCollections.observableArrayList(creatureInRange, creatureNotInRange))
          .when(mazeMock)
          .getCreatures();
      wall.setMaze(mazeMock);

      List<Bullet> bullets = tower.getBullets();
      assertEquals("There should be no bullet yet", 0, bullets.size());
      tower.shoot();
      assertEquals("There should be a bullet by that tower now", 1, bullets.size());
      Bullet shotBullet = bullets.get(0);
      assertEquals(
          "The bullet should have the correct Target", creatureInRange, shotBullet.getTarget());

      shotBullet.hitTarget();
      Mockito.verify(creatureInRange).damage(ArgumentMatchers.anyInt());
    }

    @Test
    public void fireRateTest() {
      assertEquals(level, tower.getLevel());
      int wallX = 2, wallY = 3;
      Wall wall = new Wall(wallX, wallY, tower);

      MazeModelInterface mazeMock = Mockito.mock(Maze.class);
      Creature creatureInRange = mockCreature(wallX + 0.1, wallY);
      Mockito.doReturn(FXCollections.observableArrayList(creatureInRange))
          .when(mazeMock)
          .getCreatures();
      wall.setMaze(mazeMock);

      List<Bullet> bullets = tower.getBullets();
      double delayBetweenShots = 1 / tower.getFireRate();
      tower.act(delayBetweenShots - 0.01);
      assertEquals("There should be no bullet yet", 0, bullets.size());
      tower.act(0.02);
      assertEquals("There should be a bullet by that tower now", 1, bullets.size());
      tower.act(delayBetweenShots);
      assertEquals("There should be a second bullet by that tower now", 2, bullets.size());
    }

    private Creature mockCreature(double x, double y) {
      Creature creatureMock = Mockito.mock(Creature.class);
      Mockito.doReturn(x).when(creatureMock).getX();
      Mockito.doReturn(y).when(creatureMock).getY();
      return creatureMock;
    }
  }
}
