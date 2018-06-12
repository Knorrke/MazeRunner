package model.maze;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.Bullet;
import application.model.maze.tower.TowerType;
import javafx.collections.FXCollections;

public class TowerTest {

  private AbstractTower abstractTower;

  @Test
  public void setupTest() {
    abstractTower = AbstractTower.create(TowerType.NO);
    assertNotNull("AbstractTower should not be null", abstractTower);
    assertEquals("Fire rate should be 0", 0, abstractTower.getFireRate(), 0);
  }

  @Test
  public void fireRateTest() {
    AtomicBoolean called = new AtomicBoolean(false);
    double fireRate = 5;
    abstractTower =
        new AbstractTower(fireRate, 0, 0, 0, TowerType.NO, null) {
          @Override
          public void shoot() {
            called.set(true);
          }
        };
    double delayBetweenShots = 1 / fireRate;
    abstractTower.act(delayBetweenShots / 2);
    assertFalse("Shoot should not be called yet", called.getAndSet(false));
    abstractTower.act(delayBetweenShots);
    assertTrue("Shoot should be called now", called.getAndSet(false));
  }

  @Test
  public void normalTowerTest() {
    Wall wall = Mockito.mock(Wall.class);
    abstractTower = AbstractTower.create(wall, TowerType.NORMAL);
    double fireRate = abstractTower.getFireRate();
    assertTrue(fireRate > 0);
    double delayBetweenShots = 1 / fireRate;
    abstractTower.act(delayBetweenShots);
    Mockito.verify(wall, Mockito.times(1)).getCreaturesMatchingCondition(ArgumentMatchers.any());
  }

  @Test
  public void findCreaturesInRange() {
    double visualRange = 4;
    int wallX=2, wallY=3;
    abstractTower = AbstractTower.create(TowerType.NORMAL);
    abstractTower.setVisualRange(visualRange);
    Wall wall = new Wall(wallX, wallY, abstractTower);
    
    MazeModelInterface mazeMock = Mockito.mock(Maze.class);
    Creature creatureInRange = CreatureFactory.create(mazeMock, CreatureType.NORMAL,wallX+visualRange/2, wallY);
    Creature creatureNotInRange = CreatureFactory.create(mazeMock, CreatureType.NORMAL,wallX+visualRange+1, wallY);
    Mockito.doReturn(FXCollections.observableArrayList(creatureInRange, creatureNotInRange)).when(mazeMock).getCreatures();
    wall.setMaze(mazeMock);
    
    List<Creature> creaturesInRange = abstractTower.findCreaturesInRange();
    assertEquals("one creature in range", 1, creaturesInRange.size());
    assertSame("should be the expectedCreature", creatureInRange, creaturesInRange.get(0));
  }
  
  @Test
  public void shootCreatureInRange() {
    double visualRange = 4;
    int wallX=2, wallY=3;
    abstractTower = AbstractTower.create(TowerType.NORMAL);
    abstractTower.setVisualRange(visualRange);
    Wall wall = new Wall(wallX, wallY, abstractTower);
    
    MazeModelInterface mazeMock = Mockito.mock(Maze.class);
    Creature creatureInRange = mockCreature(wallX+visualRange/2, wallY);
    Creature creatureNotInRange = mockCreature(wallX+visualRange+1, wallY);
    Mockito.doReturn(FXCollections.observableArrayList(creatureInRange, creatureNotInRange)).when(mazeMock).getCreatures();
    wall.setMaze(mazeMock);

    List<Bullet> bullets = abstractTower.getBullets();
    assertEquals("There should be no bullet yet", 0, bullets.size());
    abstractTower.shoot();
    assertEquals("There should be a bullet by that tower now", 1, bullets.size());
    Bullet shotBullet = bullets.get(0);
    assertEquals("The bullet should have the correct Target", creatureInRange, shotBullet.getTarget());
    
    shotBullet.hitTarget();
    Mockito.verify(creatureInRange).damage(ArgumentMatchers.anyInt());
  }

  private Creature mockCreature(double x, double y) {
    Creature creatureMock = Mockito.mock(Creature.class);
    Mockito.doReturn(x).when(creatureMock).getX();
    Mockito.doReturn(y).when(creatureMock).getY();
    return creatureMock;
  }
}
