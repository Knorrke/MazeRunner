package view;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import javafx.scene.input.MouseButton;

public class MazeViewTest extends AbstractViewTest {

  @Test
  public void buildWallOnClick() {
    clickOn("#maze");
    assertEquals("There should be a wall now", 1, maze.getWalls().size());
    verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"), collectInfos());
    verifyThat("#maze .wall", NodeMatchers.hasChildren(1, ".wall-image"), collectInfos());
    // second click:
    clickOn(MouseButton.PRIMARY);
    assertEquals("There should still be only one wall", 1, maze.getWalls().size());
    verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"), collectInfos());

    // third click:
    moveBy(20, 50);
    clickOn(MouseButton.PRIMARY);

    assertEquals("There should be two walls now", 2, maze.getWalls().size());
    verifyThat("#maze", NodeMatchers.hasChildren(2, ".wall"), collectInfos());
  }
}
