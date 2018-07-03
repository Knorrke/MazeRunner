package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import java.util.function.Predicate;
import org.junit.Test;
import application.controller.UserActionState;
import application.util.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.AbstractViewTest;

public class GameControllerTest extends AbstractViewTest {
  private final String build = "#buildButton", info = "#infoButton", playPause = "#playPauseButton";

  @Test
  public void playPauseButtonTest() {
    assertFalse(gameIsRunning());
    clickOn(playPause);
    assertTrue(gameIsRunning());
    clickOn(playPause);
    assertFalse(gameIsRunning());
  }

  @Test
  public void buildAndInfoButtonTest() {
    assertEquals(UserActionState.BUILD, gameController.getUserActionState());
    verifyThat(info, imageEquals(ImageLoader.infoNotSelected), collectInfos());
    verifyThat(build, imageEquals(ImageLoader.buildSelected), collectInfos());

    clickOn(info);
    assertEquals(UserActionState.INFO, gameController.getUserActionState());
    verifyThat(info, imageEquals(ImageLoader.infoSelected), collectInfos());
    verifyThat(build, imageEquals(ImageLoader.buildNotSelected), collectInfos());

    clickOn(build);
    assertEquals(UserActionState.BUILD, gameController.getUserActionState());
    verifyThat(info, imageEquals(ImageLoader.infoNotSelected), collectInfos());
    verifyThat(build, imageEquals(ImageLoader.buildSelected), collectInfos());

    interact(() -> gameController.setUserActionState(UserActionState.BLOCK_ALL));
    verifyThat(info, imageEquals(ImageLoader.infoNotSelected), collectInfos());
    verifyThat(build, imageEquals(ImageLoader.buildNotSelected), collectInfos());
  }

  private Predicate<ImageView> imageEquals(Image image) {
    return (ImageView imageview) -> imageview.getImage().equals(image);
  }

  private boolean gameIsRunning() {
    return gameLoop.runningProperty().get();
  }
}
