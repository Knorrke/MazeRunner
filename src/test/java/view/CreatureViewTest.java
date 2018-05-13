package view;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.view.creatures.CreatureView;

public class CreatureViewTest extends AbstractViewTest {
  /*@Test
  public void checkCreatureVisible() {
    game.start();
    assertEquals("There should be a creature", false, maze.getCreatures().isEmpty());
    verifyThat("#maze", NodeMatchers.hasChild(".creature"), collectInfos());
    game.pause();
  }*/

  @Test
  public void checkCreatureDirection() {
    final int forward = 0, upward = 90, backward = 180, downward = 270;
    
    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, 3, 3);
    interact(() -> maze.addCreature(creature));
    WaitForAsyncUtils.waitForFxEvents();
    verifyThat("#maze", NodeMatchers.hasChildren(1, ".creature"), collectInfos());
    CreatureView creatureView = lookup(".creature").query();
    
    interact(() -> creature.move(1, 0)); // forward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(forward, mod(creatureView.getRotate(),360));

    interact(() -> creature.move(-1, 0)); // backward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(backward, mod(creatureView.getRotate(),360));

    interact(() -> creature.move(0, 1)); // upward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(upward, mod(creatureView.getRotate(),360));

    interact(() -> creature.move(0, -1)); // downward
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(downward, mod(creatureView.getRotate(),360));
  }
  
  private int mod(double x, int modulo) {
    return Math.floorMod((int) x, modulo);
  }
}
