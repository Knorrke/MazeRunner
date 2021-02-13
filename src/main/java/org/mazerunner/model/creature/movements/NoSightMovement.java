package org.mazerunner.model.creature.movements;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.util.GraphSolver;
import org.mazerunner.util.MapNode;
import org.mazerunner.util.MazeNode;

public class NoSightMovement implements MovementInterface {
  private static final Logger LOG = Logger.getLogger(NoSightMovement.class.getName());

  @Override
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY) {
    double[][] directions =
        System.currentTimeMillis() / 1000 % 2 == 0 // randomize a bit over time
            ? new double[][] {{1, 0}, {0, 1}, {0, -1}, {-1, 0}}
            : new double[][] {{1, 0}, {0, -1}, {0, 1}, {-1, 0}};
    for (double[] direction : directions) {
      int x = (int) (currentX + direction[0]);
      int y = (int) (currentY + direction[1]);
      if (x >= 0 && y >= 0 && visited.isUnknown(x, y)) {
        return new double[] {currentX + direction[0], currentY + direction[1]};
      }
    }
    return findUnknown(visited, currentX, currentY);
  }

  private double[] findUnknown(VisitedMap visited, double currentX, double currentY) {
    MapNode start = new MazeNode(currentX, currentY);
    AtomicReference<MapNode> foundUnknown = new AtomicReference<>();
    // look in known map for closest unknown edge
    Map<MapNode, MapNode> paths =
        GraphSolver.calculatePerfectMoveMap(
            start,
            visited::isWall,
            (MapNode traversing) -> {
              if (foundUnknown.get() == null
                  && visited.isUnknown(traversing.getX(), traversing.getY())) {
                LOG.finest(
                    String.format(
                        "Found UNKNOWN field at %d,%d", traversing.getX(), traversing.getY()));
                foundUnknown.set(traversing);
              }
            });
    MapNode goal = foundUnknown.get();
    if (goal != null && paths.containsValue(start)) {
      while (paths.get(goal) != start) {
        goal = paths.get(goal);
      }
      LOG.finest(
          String.format("Moving towards unknown field, next stop %d,%d", goal.getX(), goal.getY()));
      return new double[] {goal.getX() + currentX % 1, goal.getY() + currentY % 1};
    } else {
      LOG.warning("No unknown fields! This means that there is no path to the goal.");
      return new double[] {currentX + 1, currentY};
    }
  }
}
