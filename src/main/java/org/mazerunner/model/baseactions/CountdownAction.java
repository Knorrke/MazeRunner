package org.mazerunner.model.baseactions;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class CountdownAction extends Action {
  private double countdown;
  private double defaultCountdown;
  private DoubleProperty lazyCountdownProperty;

  public CountdownAction(double defaultCountdown, double delay) {
    this(defaultCountdown);
    setCountdown(delay);
  }

  public CountdownAction(double defaultCountdown) {
    this.defaultCountdown = defaultCountdown;
    this.setCountdown(defaultCountdown);
  }

  @Override
  public final void update(double dt) {
    updateHook(dt);
    setCountdown(getCountdown() - dt);
  }

  @Override
  public boolean isFinished() {
    return getCountdown() <= 0;
  }

  protected void updateHook(double dt) {}

  protected abstract void onFinish();

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
