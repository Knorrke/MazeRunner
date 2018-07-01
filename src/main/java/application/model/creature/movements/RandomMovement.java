package application.model.creature.movements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import application.model.creature.VisitedMap;
import application.model.creature.vision.Vision;

public class RandomMovement implements MovementInterface {

  @Override
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY) {
    List<double[]> directions = new ArrayList<>(4);
    Collections.addAll(directions, new double[][] {{1, 0}, {0, 1}, {0, -1}, {-1, 0}});
    Collections.shuffle(directions);
    for (double[] direction : directions) {
      double x = currentX + direction[0];
      double y = currentY + direction[1];
      if (x >= 0 && y >= 0 && !visited.isWall((int) x, (int) y)) {
        return new double[] {x, y};
      }
    }
    return new double[] {currentX + 1, currentY};
  }
}
