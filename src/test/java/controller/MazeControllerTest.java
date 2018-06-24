package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import view.AbstractViewTest;

public class MazeControllerTest extends AbstractViewTest {
  private static final String mazeSelector = "#maze",
      buildSelector = "#buildButton",
      infoSelector = "#infoButton";

  @Test
  public void buildActionsTriggeredInBuildMode() {
    clickOn(buildSelector);
    clickOn(mazeSelector);
    assertFalse(maze.getWalls().isEmpty());
  }

  @Test
  public void buildActionsNotTriggeredInInfoMode() {
    clickOn(infoSelector);
    clickOn(mazeSelector);
    assertTrue(maze.getWalls().isEmpty());
  }

  @Test
  public void noActionInInfoModeOnEmptySquar() {
    clickOn(infoSelector);
    clickOn(mazeSelector);
    verifyThat(".popover", NodeMatchers.isNull(), collectInfos());
  }

  @Test
  public void wallInfoTriggeredInInfoMode() {
    clickOn(buildSelector);
    clickOn(mazeSelector);
    clickOn(infoSelector);
    clickOn(".wall");
    verifyThat(".popover", NodeMatchers.isNotNull(), collectInfos());
  }
  
  @Test
  public void creatureInfoNotTriggeredInBuildMode() {
    interact(() -> maze.addCreature(CreatureFactory.create(maze, CreatureType.NORMAL, 2, 3)));
    clickOn(buildSelector);
    clickOn(".creature");
    verifyThat(".popover", NodeMatchers.isNull(), collectInfos());
  }
  
  @Test
  public void creatureInfoTriggeredInInfoMode() {
    interact(() -> maze.addCreature(CreatureFactory.create(maze, CreatureType.NORMAL, 2, 3)));
    clickOn(infoSelector);
    clickOn(".creature");
    verifyThat(".popover", NodeMatchers.isNotNull(), collectInfos());
  }
}
