package application.util;

import application.model.Position;

public final class Calc {
  public static double calculateRotation(Position oldPos, Position newPos) {
    return Math.atan2(newPos.getY() - oldPos.getY(), newPos.getX() - oldPos.getX()) / Math.PI * 180;
  }
}