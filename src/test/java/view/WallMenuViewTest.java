package view;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.TowerType;
import application.view.maze.WallMenuView;
import application.view.maze.WallView;
import javafx.scene.input.MouseButton;

public class WallMenuViewTest extends AbstractViewTest {
  WallMenuView menu;

  @Before
  public void setup() {
    buildWall();
    openMenuAndWaitForAnimation();
  }
  
  @Test
  public void openingMenuTest() {
    verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"), collectInfos());
    assertTrue(menu.isShown());
  }

  @Test
  public void closingMenuTest() {
    moveTo(0, 0);
    assertFalse(menu.isShown());
  }
  
  @Test
  public void sellTest() {
    int moneyBefore = player.getMoney();
    selectSell();
    assertFalse(menu.isShown());
    verifyThat("#maze", NodeMatchers.hasChildren(0, ".wall"), collectInfos());
    assertTrue(player.getMoney() > moneyBefore);
    verifyThat(
        "#money", LabeledMatchers.hasText(Integer.toString(player.getMoney())), collectInfos());
  }
  
  @Test
  public void optionShouldHaveLabel() {
    int costs = AbstractTower.create(TowerType.NORMAL).getCosts();
    verifyThat("#normal-tower-label", LabeledMatchers.hasText("-$"+costs));
    int earnings = maze.getWalls().get(0).getCosts();
    verifyThat("#sell-label", LabeledMatchers.hasText("+$"+earnings));
  }

  @Test
  public void buildTowerTest() {
    int moneyBefore = player.getMoney();
    int costs = AbstractTower.create(TowerType.NORMAL).getCosts();
    selectNormalTower();
    assertFalse(menu.isShown());
    verifyThat(
        ".wall", NodeMatchers.hasChildren(1, "." + TowerType.NORMAL.toString()), collectInfos());
    assertTrue(player.getMoney() == moneyBefore - costs);
    verifyThat(
        "#money", LabeledMatchers.hasText(Integer.toString(player.getMoney())), collectInfos());
  }
  
  @Test
  public void sellAfterBuildTest() {
    int moneyBefore = player.getMoney();
    selectNormalTower();
    waitForAnimation();
    openMenuAndWaitForAnimation();
    selectSell();
    waitForAnimation();
    buildWall();
    assertEquals("should have got money for tower back", moneyBefore, player.getMoney());
  }
  
  /*
   * Helper methods
   */

  private void selectNormalTower() {
    clickOn("#normal-tower");
  }

  private void selectSell() {
    clickOn("#sell");
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
    WaitForAsyncUtils.sleep((long) menu.getAnimationDuration().toMillis(), TimeUnit.MILLISECONDS);
  }
}
