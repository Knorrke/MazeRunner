package view.maze;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.view.creatures.CreatureView;
import view.AbstractViewTest;

public class CreatureViewTest extends AbstractViewTest {
  @Test
  public void checkCreatureVisible() {
    interact(() -> level.sendNextCreatureWave());
    assertEquals("There should be a creature", false, maze.getCreatures().isEmpty());
    verifyThat("#maze", NodeMatchers.hasChild(".creature"), collectInfos());
  }

  @Test
  public void checkCreatureDirection() {
    final int forward = 0, upward = 90, backward = 180, downward = 270;

    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, 3, 3);
    interact(() -> maze.addCreature(creature));
    WaitForAsyncUtils.waitForFxEvents();
    verifyThat("#maze", NodeMatchers.hasChildren(1, ".creature"), collectInfos());
    CreatureView creatureView = lookup(".creature").query();

    interact(() -> creature.moveBy(1, 0)); // forward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(forward, mod(creatureView.getRotate(), 360));

    interact(() -> creature.moveBy(-1, 0)); // backward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(backward, mod(creatureView.getRotate(), 360));

    interact(() -> creature.moveBy(0, 1)); // upward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(upward, mod(creatureView.getRotate(), 360));

    interact(() -> creature.moveBy(0, -1)); // downward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(downward, mod(creatureView.getRotate(), 360));
  }

  @Test
  public void creatureRemovedAfterReachingGoal() {
    Creature creature =
        CreatureFactory.create(maze, CreatureType.NORMAL, maze.getMaxWallX() - 1, 3);
    interact(() -> maze.addCreature(creature));
    verifyThat("#maze", NodeMatchers.hasChildren(1, ".creature"), collectInfos());
    interact(() -> maze.update(1));
    verifyThat(".creature", NodeMatchers.isNull(), collectInfos());
  }

  private int mod(double x, int modulo) {
    return Math.floorMod((int) x, modulo);
  }
}
