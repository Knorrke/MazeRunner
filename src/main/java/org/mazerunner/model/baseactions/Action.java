package org.mazerunner.model.baseactions;

import org.mazerunner.controller.gameloop.ActorInterface;

public abstract class Action implements ActorInterface {
  private boolean onFinishHandled = false;

  @Override
  public final void act(double dt) {
    if (!isFinished()) {
      update(dt);
      if (isFinished()) {
        onFinishHandled = true;
        onFinish();
      }
    } else if (!onFinishHandled) {
      onFinishHandled = true;
      onFinish();
    }
  }

  protected abstract void update(double dt);

  public abstract boolean isFinished();

  protected abstract void onFinish();
}
