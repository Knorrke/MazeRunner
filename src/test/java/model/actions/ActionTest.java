package model.actions;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.mazerunner.model.baseactions.CountdownAction;

public class ActionTest {

  @Test
  public void verifyHooksGetCalled() {
    double shortTime = 0.001;
    double longTime = 20;
    AtomicBoolean updateHookCalled = new AtomicBoolean(false);
    AtomicBoolean executeCalled = new AtomicBoolean(false);

    CountdownAction countdownAction =
        new CountdownAction((shortTime + longTime) / 2) {
          @Override
          public void updateHook(double dt) {
            updateHookCalled.set(true);
          }

          @Override
          public void onFinish() {
            executeCalled.set(true);
          }
        };
    countdownAction.act(shortTime);
    assertTrue(updateHookCalled.get());
    assertFalse("execute shouldn't be called before countdown", executeCalled.get());

    updateHookCalled.set(false);
    executeCalled.set(false);
    countdownAction.act(longTime);
    assertTrue(updateHookCalled.get());
    assertTrue(executeCalled.get());
  }

  @Test
  public void resetCountdownTest() {
    double countdown = 1;
    AtomicInteger callsToExecuteWithoutReset = new AtomicInteger(0);
    AtomicInteger callsToExecuteWithReset = new AtomicInteger(0);

    CountdownAction actionWithoutReset =
        new CountdownAction(countdown) {
          @Override
          public void onFinish() {
            callsToExecuteWithoutReset.getAndIncrement();
          }
        };

    CountdownAction actionWithReset =
        new CountdownAction(countdown) {
          @Override
          public void onFinish() {
            resetCountdown();
            callsToExecuteWithReset.getAndIncrement();
          }
        };

    actionWithoutReset.act(countdown / 2);
    actionWithReset.act(countdown / 2);
    assertEquals("Execute shouldn't be called yet", 0, callsToExecuteWithoutReset.get());
    assertEquals("Execute shouldn't be called yet", 0, callsToExecuteWithReset.get());

    actionWithoutReset.act(countdown);
    actionWithReset.act(countdown);
    assertEquals("Execute should be called now", 1, callsToExecuteWithoutReset.get());
    assertEquals("Execute should be called now", 1, callsToExecuteWithReset.get());

    actionWithoutReset.act(countdown);
    actionWithReset.act(countdown);
    assertEquals("Execute should still be called only once", 1, callsToExecuteWithoutReset.get());
    assertEquals("Execute should be called twice now", 2, callsToExecuteWithReset.get());
  }
}
