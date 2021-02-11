package model.maze.tower;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import javafx.collections.FXCollections;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.model.maze.tower.bullet.Bullet;
import org.mazerunner.model.maze.tower.bullet.DamageBullet;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class FastTowerTest {

  private Wall wallMock;
  private AbstractTower tower;
  private Creature creatureInRange;

  @Before
  public void setUp() {
    wallMock = Mockito.mock(Wall.class);
    tower = AbstractTower.create(wallMock, TowerType.FAST);
    creatureInRange =
        Mockito.spy(CreatureFactory.create(Mockito.mock(Maze.class), CreatureType.NORMAL, 0, 0));
    Mockito.doReturn(FXCollections.observableArrayList(creatureInRange))
        .when(wallMock)
        .getCreaturesMatchingCondition(ArgumentMatchers.any());
  }

  @Test
  public void fastTowerTest() {
    double fireRate = tower.getFireRate();
    assertTrue(
        "should be faster than normal tower",
        fireRate > AbstractTower.create(TowerType.NORMAL).getFireRate());
    double delayBetweenShots = 1 / fireRate;
    tower.act(delayBetweenShots);
    Mockito.verify(wallMock, Mockito.times(1))
        .getCreaturesMatchingCondition(ArgumentMatchers.any());
  }

  @Test
  public void shootsDamageBulletsTest() {
    tower.shoot();
    Bullet bullet = tower.getBullets().get(0);
    assertThat(bullet, is(instanceOf(DamageBullet.class)));
  }

  @Test
  public void fastTowerUpgradeTest() {
    int costsBefore = tower.getCosts();
    double fireRate = tower.getFireRate();
    TowerType type = tower.getType();
    assertEquals(0, tower.getLevel());

    tower = tower.upgrade();
    assertNotEquals(costsBefore, tower.getCosts());
    assertNotEquals(fireRate, tower.getFireRate());
    assertEquals(type, tower.getType());
    assertEquals(1, tower.getLevel());
  }

  @Test
  public void hasFourUpgradesTest() {
    int upgrades;
    for (upgrades = 0; upgrades < 4 && tower.getNextUpgrade() != null; upgrades++) {
      tower = tower.upgrade();
    }
    assertEquals("Should have (at least) four upgrades", 4, upgrades);
  }
}
