package model.maze;

import static org.junit.Assert.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.NoTower;
import org.mazerunner.model.maze.tower.TowerType;
import org.mockito.Mockito;

public class WallTest {
  private int x, y;
  private Wall wall;

  @Before
  public void setup() {
    x = 2;
    y = 3;
    wall = new Wall(x, y, Mockito.mock(NoTower.class));
  }

  @Test
  public void wallConstructorSetsPosition() {
    assertEquals("X position should be as passed in constructor", x, wall.getX());
    assertEquals("Y position should be as passed in constructor", y, wall.getY());
    assertEquals("Position should be returned as  int array", 2, wall.getPosition().length);
    assertArrayEquals(
        "2-dim array position should be as it was passed in constructor",
        new int[] {x, y},
        wall.getPosition());
  }

  @Test
  public void wallUsesIntegerProperties() {
    assertEquals("X position should be as passed in constructor", x, wall.xProperty().get());
    assertEquals("Y position should be as passed in constructor", y, wall.yProperty().get());
  }

  @Test
  public void newWallHasNoTower() {
    assertTrue("New wall should have NoTower ", wall.getTower() instanceof NoTower);
  }

  @Test
  public void listeningToTowerChangeShouldWork() {
    BooleanProperty changed = new SimpleBooleanProperty(false);
    wall.towerProperty()
        .addListener(
            (obs, oldTower, newTower) -> {
              changed.set(true);
            });
    assertFalse("tower should not have changed yet", changed.get());

    wall.getTower().shoot();
    assertFalse("tower should not have changed yet", changed.get());

    wall.setTower(AbstractTower.create(wall, TowerType.NORMAL));
    assertTrue("AbstractTower should have changed", changed.get());
  }
}
