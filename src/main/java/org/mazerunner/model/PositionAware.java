package org.mazerunner.model;

public interface PositionAware {
  public double getX();

  public double getY();

  public static PositionAware valueOf(double[] goal) {
    return new PositionAware() {
      @Override
      public double getX() {
        return goal[0];
      }

      @Override
      public double getY() {
        return goal[1];
      }
    };
  }
}
