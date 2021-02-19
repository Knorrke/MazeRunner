package view.maze;

import static org.junit.Assert.assertEquals;

import javafx.scene.image.ImageView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.tower.TowerType;
import org.testfx.util.WaitForAsyncUtils;
import view.AbstractViewTest;

@RunWith(Parameterized.class)
public class TowerViewTest extends AbstractViewTest {

  @Parameters(name = "{index}: Creature on ({0},{1}) should be targeted at {2}°")
  public static Object[][] data() {
    return new Object[][] {
      {2.5, 3.5, 0},
      {2.5, 2.5, 45},
      {3.5, 2.5, 90},
      {4.5, 2.5, 135},
      {4.5, 3.5, 180},
      {4.5, 4.5, 225},
      {3.5, 4.5, 270},
      {2.5, 4.5, 315}
    };
  }

  @Parameter(0)
  public double creatureX;

  @Parameter(1)
  public double creatureY;

  @Parameter(2)
  public int expectedAngle;

  @Test
  public void rotationTest() {
    Creature c = CreatureFactory.create(maze, CreatureType.NORMAL, creatureX, creatureY);
    c.setVelocity(0);
    interact(
        () -> {
          maze.addCreature(c);
          maze.buildWall(3, 3);
          maze.buildTower(maze.getWallOn(3, 3), TowerType.NORMAL);
        });

    ImageView towerImage = lookup(".tower-image").query();
    assertEquals(
        "default orientation 0 degree", 0, Math.floorMod((int) towerImage.getRotate(), 360));

    interact(
        () -> {
          maze.update(1);
        });
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(
        "Orientation should change after shot",
        expectedAngle,
        Math.floorMod((int) towerImage.getRotate(), 360));
  }
}
