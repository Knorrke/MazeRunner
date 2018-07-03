package org.mazerunner.controller.gameloop;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameLoop extends AnimationTimer {
  public static double executionSpeedUp = 1;
  private Updateable main;
  private BooleanProperty running;

  public GameLoop(Updateable main) {
    this.main = main;
    running = new SimpleBooleanProperty(false);
  }

  private static final float timeStep = 0.0166f;
  private static final float maxTimeStep = 0.05f;
  private long previousTime = 0;
  private float accumulatedTime = 0;

  @Override
  public void handle(long currentTime) {
    if (previousTime == 0) {
      previousTime = currentTime;
      return;
    }

    float secondsElapsed = (currentTime - previousTime) / 1e9f; /* nanoseconds to seconds */
    float secondsElapsedCapped = Math.min(secondsElapsed, maxTimeStep);
    accumulatedTime += secondsElapsedCapped;
    previousTime = currentTime;

    while (accumulatedTime >= timeStep) {
      main.update(executionSpeedUp * timeStep);
      accumulatedTime -= timeStep;
    }
  }

  @Override
  public void start() {
    running.set(true);
    super.start();
  }

  @Override
  public void stop() {
    running.set(false);
    previousTime = 0;
    accumulatedTime = 0;
    super.stop();
  }

  public BooleanProperty runningProperty() {
    return running;
  }

  public void togglePlayPause() {
    if (running.get()) stop();
    else start();
  }
}
