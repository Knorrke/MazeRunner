package application.model.actions;

public abstract class Action {
  private double countdown;
  private double defaultCountdown;

  public Action() {
    this(0);
  }

  public Action(double countdown) {
    this.setCountdown(countdown);
    defaultCountdown = countdown;
  }

  public void run(double dt) {
    updateHook(dt);
    if (countdown >= 0 && countdown < dt) {
      execute();
    }
    countdown -= dt;
  }

  public void updateHook(double dt) {}

  public abstract void execute();

  public void resetCountdown() {
    setCountdown(defaultCountdown);
  }

  private void setCountdown(double countdown) {
    this.countdown = countdown;
  }
}
