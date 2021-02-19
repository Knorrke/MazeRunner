package view.maze;

import static org.junit.Assert.assertEquals;

import javafx.scene.image.ImageView;
import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.tower.TowerType;
import org.testfx.util.WaitForAsyncUtils;
import view.AbstractViewTest;

public class TowerViewTest extends AbstractViewTest {

  @Test
  public void rotationTest() {
    Creature c = CreatureFactory.create(maze, CreatureType.NORMAL, 2.5, 2.5);
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
        45,
        Math.floorMod((int) towerImage.getRotate(), 360));
  }
}
