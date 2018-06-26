package model.maze.tower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.hamcrest.core.Is.*;

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
import application.model.maze.tower.bullet.Bullet;
import application.model.maze.tower.bullet.SlowdownBullet;
import javafx.collections.FXCollections;

public class SlowdownTowerTest {
  
  private Wall wallMock;
  private AbstractTower tower;
  private Creature creatureInRange;
  
  @Before
  public void setUp() {
    wallMock = Mockito.mock(Wall.class);
    tower = AbstractTower.create(wallMock, TowerType.SLOWDOWN);
    creatureInRange = Mockito.spy(CreatureFactory.create(Mockito.mock(Maze.class), CreatureType.NORMAL,0,0));
    Mockito.doReturn(FXCollections.observableArrayList(creatureInRange))
        .when(wallMock)
        .getCreaturesMatchingCondition(ArgumentMatchers.any());
  }
  
  @Test
  public void slowdownTowerTest() {
    double fireRate = tower.getFireRate();
    assertTrue(fireRate > 0);
    double delayBetweenShots = 1 / fireRate;
    tower.act(delayBetweenShots);
    Mockito.verify(wallMock, Mockito.times(1)).getCreaturesMatchingCondition(ArgumentMatchers.any());
  }
  
  @Test
  public void shootsSlowdownBulletsTest() {
    tower.shoot();
    Bullet bullet = tower.getBullets().get(0);
    assertThat(bullet, is(instanceOf(SlowdownBullet.class)));
  }
  

  @Test
  public void slowdownTowerUpgradeTest() {
    tower.shoot();
    Bullet bulletBeforeUpgrade = tower.getBullets().get(0);
    tower.getBullets().clear();
    AbstractTower upgraded = tower.upgrade();
    upgraded.shoot();
    Bullet bulletAfterUpgrade = upgraded.getBullets().get(0);
    assertNotEquals(bulletBeforeUpgrade, bulletAfterUpgrade);
    
    double velocityBefore = creatureInRange.getVelocity();
    bulletBeforeUpgrade.hitTarget();
    Mockito.verify(creatureInRange).slowdown(ArgumentMatchers.anyDouble());
    double velocityAfterNormalShot = creatureInRange.getVelocity();
    assertTrue("Should slow creature down", velocityAfterNormalShot < velocityBefore);
    while(!bulletBeforeUpgrade.isOver()) {      
      bulletBeforeUpgrade.act(500);
    }
    assertEquals(velocityBefore, creatureInRange.getVelocity(), 0.001);
    
    Mockito.clearInvocations(creatureInRange);
    bulletAfterUpgrade.hitTarget();
    Mockito.verify(creatureInRange).slowdown(ArgumentMatchers.anyDouble());
    double velocityAfterUpgradedShot = creatureInRange.getVelocity();
    assertTrue(velocityAfterUpgradedShot < velocityBefore);
    assertTrue("Should slow creature down even more than normal shot", velocityAfterUpgradedShot < velocityAfterNormalShot);
    while(!bulletAfterUpgrade.isOver()) {      
      bulletAfterUpgrade.act(500);
    }
    assertEquals(velocityBefore, creatureInRange.getVelocity(), 0.001);
  }
}
