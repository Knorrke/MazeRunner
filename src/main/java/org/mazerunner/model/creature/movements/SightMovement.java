package org.mazerunner.model.creature.movements;

import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;

public class SightMovement implements MovementInterface {

  @Override
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY) {
    vision.updateVisitedMap(visited, currentX, currentY);
    return new NoSightMovement().getNextGoal(null, visited, currentX, currentY);
  }
}
