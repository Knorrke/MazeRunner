package org.mazerunner.model.baseactions;

import org.mazerunner.controller.gameloop.ActorInterface;

public abstract class Action implements ActorInterface {
  private boolean onFinishedCalled = false;

  @Override
  public final void act(double dt) {
    if (!isFinished()) {
      update(dt);
      if (isFinished()) {
        onFinish();
        onFinishedCalled = true;
      }
    } else if (!onFinishedCalled) {
      onFinish();
      onFinishedCalled = true;
    }
  }

  protected abstract void update(double dt);

  public abstract boolean isFinished();

  protected abstract void onFinish();
}
