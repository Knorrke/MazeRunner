package org.mazerunner.model.creature.vision;

import java.util.List;
import org.mazerunner.model.creature.VisitedMap;

public class Vision {
  private double radius;

  public Vision(double radius) {
    this.radius = radius;
  }

  public void updateVisitedMap(VisitedMap visited, double currentX, double currentY) {
    List<int[]> edges = null;
  }
}
