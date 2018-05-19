package view;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import application.view.maze.WallMenuView;
import application.view.maze.WallView;
import javafx.scene.input.MouseButton;

public class WallMenuViewTest extends AbstractViewTest {
  WallMenuView menu;

  @Test
  public void openingMenuTest() {
    buildWall();
    verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"), collectInfos());
    openMenuAndWaitForAnimation();
    assertTrue(menu.isShown());
  }
  
  @Test
  public void closingMenuTest() {
    buildWall();
    openMenuAndWaitForAnimation();
    moveTo(0,0);
    assertFalse(menu.isShown());
  }
  
  @Test
  public void sellTest() {
    buildWall();
    openMenuAndWaitForAnimation();
    selectSell();
    assertFalse(menu.isShown());
    verifyThat("#maze", NodeMatchers.hasChildren(0, ".wall"), collectInfos());
  }
  
  private void selectSell() {
    clickOn("#sell-item");
  }

  private void buildWall() {
    clickOn("#maze", MouseButton.PRIMARY);
  }
  
  private void openMenuAndWaitForAnimation() {
    clickOn(".wall", MouseButton.PRIMARY);
    WallView wall = this.lookup(".wall").query();
    menu = wall.getWallMenu();
    
    waitForAnimation();
  }
  
  private void waitForAnimation() {
    WaitForAsyncUtils.sleep((long)menu.getAnimationDuration().toMillis(), TimeUnit.MILLISECONDS);
  }
}
