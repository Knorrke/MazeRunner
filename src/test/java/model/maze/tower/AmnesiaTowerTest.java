package model.maze.tower;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.Maze;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.TowerType;
import application.model.maze.tower.bullet.AmnesiaBullet;
import application.model.maze.tower.bullet.Bullet;
import javafx.collections.FXCollections;

public class AmnesiaTowerTest {
  private Wall wallMock;
  private AbstractTower tower;
  private Creature creatureInRange;

  @Before
  public void setUp() {
    wallMock = Mockito.mock(Wall.class);
    tower = AbstractTower.create(wallMock, TowerType.AMNESIA);
    creatureInRange =
        Mockito.spy(CreatureFactory.create(Mockito.mock(Maze.class), CreatureType.NORMAL, 0, 0));
    Mockito.doReturn(FXCollections.observableArrayList(creatureInRange))
        .when(wallMock)
        .getCreaturesMatchingCondition(ArgumentMatchers.any());
  }

  @Test
  public void amnesiaTowerTest() {
    double fireRate = tower.getFireRate();
    assertTrue(fireRate > 0);
    double delayBetweenShots = 1 / fireRate;
    tower.act(delayBetweenShots);
    Mockito.verify(wallMock, Mockito.times(1))
        .getCreaturesMatchingCondition(ArgumentMatchers.any());
  }

  @Test
  public void shootsAmnesiaBulletsTest() {
    tower.shoot();
    Bullet bullet = tower.getBullets().get(0);
    assertThat(bullet, is(instanceOf(AmnesiaBullet.class)));
  }

  @Test
  public void hasNoUpgradeTest() {
    assertNull("Shouldn't have upgrades", tower.getNextUpgrade());
  }
}
