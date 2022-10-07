package org.mazerunner.model.creature.movements;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.model.maze.GraphSolver;
import org.mazerunner.model.maze.MapNode;
import org.mazerunner.model.maze.MazeNode;

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
    return findGoalOrUnknown(visited, currentX, currentY);
  }

  private double[] findGoalOrUnknown(VisitedMap visited, double currentX, double currentY) {
    MapNode start = new MazeNode((int) currentX, (int) currentY);
    AtomicReference<MapNode> foundUnknown = new AtomicReference<>();
    AtomicReference<MapNode> foundGoal = new AtomicReference<>();
    // look in known map for closest unknown field
    Map<MapNode, MapNode> paths =
        GraphSolver.calculateShortestPaths(
            start,
            visited::isWall,
            (MapNode traversing) -> {
              if (foundGoal.get() != null) return;
              else if (traversing.isGoal()) {
                LOG.log(
                    Level.FINEST,
                    "Found goal at {0},{1}",
                    new Object[] {traversing.getX(), traversing.getY()});
                foundGoal.set(traversing);
              } else if (foundUnknown.get() == null
                  && visited.isUnknown(traversing.getX(), traversing.getY())) {
                LOG.log(
                    Level.FINEST,
                    "Found unknown field at {0},{1}",
                    new Object[] {traversing.getX(), traversing.getY()});
                foundUnknown.set(traversing);
              }
            });
    MapNode nextGoal = foundGoal.get() != null ? foundGoal.get() : foundUnknown.get();
    if (nextGoal != null && paths.containsValue(start)) {
      while (paths.get(nextGoal) != start) {
        nextGoal = paths.get(nextGoal);
      }
      LOG.log(
          Level.FINEST,
          "Moving towards unknown field, next stop {0},{1}",
          new Object[] {nextGoal.getX(), nextGoal.getY()});
      return new double[] {nextGoal.getX() + currentX % 1, nextGoal.getY() + currentY % 1};
    } else {
      LOG.warning("No unknown fields! This means that there is no path to the goal.");
      return new double[] {currentX + 1, currentY};
    }
  }
}
