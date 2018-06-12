package application.model.actions;

public abstract class Action {
  private double countdown;
  private double defaultCountdown;

  public Action(double defaultCountdown) {
    this.defaultCountdown = defaultCountdown;
    this.setCountdown(defaultCountdown);
  }

  public void run(double dt) {
    updateHook(dt);
    if (countdown >= 0 && countdown <= dt) {
      execute();
    }
    countdown -= dt;
  }

  protected void updateHook(double dt) {}

  protected abstract void execute();

  protected void resetCountdown() {
    setCountdown(defaultCountdown);
  }

  protected void setCountdown(double countdown) {
    this.countdown = countdown;
  }
}
