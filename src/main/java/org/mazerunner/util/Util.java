package org.mazerunner.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import org.mazerunner.model.Position;

public final class Util {
  private Util() {}

  public static double calculateRotation(Position oldPos, Position newPos) {
    return Math.atan2(newPos.getY() - oldPos.getY(), newPos.getX() - oldPos.getX()) / Math.PI * 180;
  }

  public static StringBinding moneyString(IntegerProperty moneyProperty) {
    return Bindings.createStringBinding(() -> Util.moneyString(moneyProperty.get()), moneyProperty);
  }

  public static String moneyString(int money) {
    return String.format(getMoneyFormatString(money), money);
  }

  private static String getMoneyFormatString(int i) {
    return i > 0 ? "+%d$" : "%d$";
  }

  public static double round(double val, int decimalPlaces) {
    int[] factors = {1, 10, 100, 1000, 1000, 10000, 100000};
    double factor =
        decimalPlaces < factors.length ? factors[decimalPlaces] : Math.pow(10, decimalPlaces);
    return Math.round(val * factor) / factor;
  }

  public static double distance(double x1, double y1, double x2, double y2) {
    return Math.hypot(x2 - x1, y2 - y1);
  }
}
