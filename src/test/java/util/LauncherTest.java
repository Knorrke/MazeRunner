package util;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import application.Launcher;
import application.controller.GameController;

public class LauncherTest {
  
  @Test
  public void loadSavegameTest() {
    String filepath = "testSetups/testSetup.json";
    assertNotNull("File should exist and be found by classLoader", getClass().getClassLoader().getResource(filepath));
    GameController controller = Launcher.createGame(filepath);
    assertNotNull(controller);
    assertNotNull(controller.getModel());
  }
}
