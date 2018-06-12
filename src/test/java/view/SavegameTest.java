package view;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import application.Launcher;
import application.controller.GameController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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
    Mockito.verifyZeroInteractions(mock);
    push(KeyCode.CONTROL, KeyCode.S);
    Mockito.verify(mock)
        .handle(
            ArgumentMatchers.argThat(
                event -> event.getCode() == KeyCode.S && event.isControlDown()));
  }
}
