package application.model.baseactions;

import application.controller.gameloop.ActorInterface;

public abstract class Action implements ActorInterface {
  @Override
  public final void act(double dt) {
    if (!isFinished()) {
      update(dt);
      if (isFinished()) {
        onFinish();
      }
    }
  }

  protected abstract void update(double dt);

  public abstract boolean isFinished();

  protected abstract void onFinish();
}
