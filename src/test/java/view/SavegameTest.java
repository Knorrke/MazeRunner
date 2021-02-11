package view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.Test;
import org.mazerunner.Launcher;
import org.mazerunner.controller.GameController;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class SavegameTest extends AbstractViewTest {

  private Stage primaryStage;

  @Override
  public void start(Stage stage) {
    primaryStage = stage;
  }

  @Test
  public void loadSavegameTest() {
    String filepath = "testSetups/testSetup.json";
    assertNotNull(
        "File should exist and be found by classLoader",
        getClass().getClassLoader().getResource(filepath));
    GameController controller = Launcher.createGame(filepath);
    assertNotNull(controller);
    assertNotNull(controller.getModel());
  }

  @Test
  public void createSavegameTest() {
    GameController gameController = Launcher.createGame();
    interact(() -> Launcher.startScene(gameController, primaryStage));
    @SuppressWarnings("unchecked")
    EventHandler<KeyEvent> mock = Mockito.mock(EventHandler.class);
    gameController.addSaveHandler(mock);
    press(KeyCode.A);
    Mockito.verifyNoInteractions(mock);
    push(KeyCode.CONTROL, KeyCode.S);
    ArgumentCaptor<KeyEvent> argument = ArgumentCaptor.forClass(KeyEvent.class);
    Mockito.verify(mock).handle(argument.capture());
    assertEquals(KeyCode.S, argument.getValue().getCode());
    assertTrue(argument.getValue().isControlDown());
  }
}
