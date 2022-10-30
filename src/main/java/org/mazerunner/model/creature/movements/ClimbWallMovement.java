package org.mazerunner.model.creature.movements;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.model.maze.GraphSolver;
import org.mazerunner.model.maze.MapNode;
import org.mazerunner.model.maze.MazeNode;

public class ClimbWallMovement implements MovementInterface {
  private static final Logger LOG = Logger.getLogger(ClimbWallMovement.class.getName());

  @Override
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY) {
    vision.updateVisitedMap(visited, currentX, currentY);
    MapNode start = new MazeNode((int) currentX, (int) currentY);
    if (visited.isWall((int) currentX, (int) currentY)
        && !visited.isTower((int) currentX, (int) currentY))
      return new double[] {currentX, currentY};
    AtomicReference<MapNode> foundUnknown = new AtomicReference<>();
    AtomicReference<MapNode> foundGoal = new AtomicReference<>();
    Map<MapNode, MapNode> paths =
        GraphSolver.calculateShortestPaths(
            start,
            (x, y) -> !visited.checkBounds(x, y) || visited.isTower(x, y),
            (MapNode traversing) -> {
              int x = traversing.getX();
              int y = traversing.getY();
              if (foundGoal.get() != null) return;
              else if (traversing.isGoal()) foundGoal.set(traversing);
              else if (visited.isWall(x, y) && !visited.isTower(x, y)) foundGoal.set(traversing);
              else if (foundUnknown.get() != null) return;
              else if (visited.isUnknown(x, y)) foundUnknown.set(traversing);
            });
    MapNode nextGoal = foundGoal.get() != null ? foundGoal.get() : foundUnknown.get();
    if (nextGoal != null && paths.containsValue(start)) {
      while (paths.get(nextGoal) != start) {
        nextGoal = paths.get(nextGoal);
      }
      return new double[] {nextGoal.getX() + currentX % 1, nextGoal.getY() + currentY % 1};
    } else {
      LOG.warning("No empty walls and no path to the goal!");
      return new double[] {currentX + 1, currentY};
    }
  }
}
