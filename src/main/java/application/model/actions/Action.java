package application.model.actions;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class Action {
  private double countdown;
  private double defaultCountdown;
  private DoubleProperty lazyCountdownProperty;

  public Action(double defaultCountdown) {
    this.defaultCountdown = defaultCountdown;
    this.setCountdown(defaultCountdown);
  }

  public void run(double dt) {
    updateHook(dt);
    if (getCountdown() >= 0 && getCountdown() <= dt) {
      execute();
    }
    setCountdown(getCountdown() - dt);
  }

  protected void updateHook(double dt) {}

  protected abstract void execute();

  public void resetCountdown() {
    setCountdown(defaultCountdown);
  }

  protected void setCountdown(double countdown) {
    if (lazyCountdownProperty == null) {
      this.countdown = countdown;
    } else {
      lazyCountdownProperty.set(countdown);
    }
  }

  public double getCountdown() {
    if (lazyCountdownProperty == null) {
      return countdown;
    } else {
      return lazyCountdownProperty.get();
    }
  }

  public DoubleProperty countdownProperty() {
    if (lazyCountdownProperty == null) {
      lazyCountdownProperty = new SimpleDoubleProperty(countdown);
    }
    return lazyCountdownProperty;
  }
}
