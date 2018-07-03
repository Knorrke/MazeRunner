package view.creatures;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.creature.actions.TalkAction;
import org.mockito.Mockito;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;
import javafx.scene.image.ImageView;
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
    ImageView creatureView = lookup(".creature > .image-view").query();

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

    interact(() -> creature.moveBy(0.5, 0.5));
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(45, mod(creatureView.getRotate(), 360));

    interact(() -> creature.moveBy(Math.cos(60 * Math.PI / 180), Math.sin(60 * Math.PI / 180)));
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(60, mod(creatureView.getRotate(), 360), 0.01);
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

  @Test
  public void creatureHealthBarAfterDamage() {
    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, 3, 3);
    interact(() -> maze.addCreature(creature));
    verifyThat(".health-bar", anyOf(NodeMatchers.isNull(), NodeMatchers.isInvisible()));
    interact(() -> creature.damage(creature.getLifes() - 1));
    verifyThat(".creature", NodeMatchers.isNotNull(), collectInfos());
    verifyThat(".health-bar", NodeMatchers.isNotNull());
    verifyThat(".health-bar", NodeMatchers.isVisible());
  }

  @Test
  public void speechBubbleWhenTalking() {
    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, 3, 3);
    interact(() -> maze.addCreature(creature));
    verifyThat(".speech-bubble", NodeMatchers.isInvisible());
    interact(() -> creature.setAction(new TalkAction(200, Mockito.mock(Creature.class))));
    verifyThat(".speech-bubble", NodeMatchers.isVisible());
  }

  private int mod(double x, int modulo) {
    return Math.floorMod((int) x, modulo);
  }
}
