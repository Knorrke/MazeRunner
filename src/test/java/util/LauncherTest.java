package util;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import application.Launcher;

@RunWith(Parameterized.class)
public class LauncherTest {

  @Parameters(name = "{index}: Launch with \"{0}\"")
  public static Object[] data() {
    return new Object[] {null, "--setup=maze"};
  }

  @After
  public void cleanUp() throws TimeoutException {
    FxToolkit.hideStage();
  }

  @Parameter public String fileArg;

  @Test
  public void launchTest() {
    AtomicBoolean success = new AtomicBoolean(false);
    Thread thread =
        new Thread(
            () -> {
              try {
                ApplicationTest.launch(Launcher.class, fileArg);
                success.set(true);
              } catch (Throwable e) {
                if (e.getCause() != null
                    && e.getCause().getClass().equals(InterruptedException.class)) {
                  // We expect to get this exception since we interrupted the JavaFX application.
                  success.set(true);
                  return;
                }
                // This is not the exception we are looking for.
                Logger.getLogger(LauncherTest.class.getName()).log(Level.SEVERE, null, e);
              }
            });
    thread.start();
    try {
      Thread.sleep(5000); // Wait for 5 seconds before interrupting JavaFX application
    } catch (InterruptedException ignore) {
    }
    thread.interrupt();
    try {
      thread.join(1); // Wait 1 second for our wrapper thread to finish.
    } catch (InterruptedException ignore) {
    }
    assertTrue(success.get());
  }
}
