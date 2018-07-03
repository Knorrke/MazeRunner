package org.mazerunner.model;

public interface Moveable extends PositionAware {
  public double getVelocity();

  public void moveBy(double dx, double dy);

  public void moveTo(double x, double y);
}
