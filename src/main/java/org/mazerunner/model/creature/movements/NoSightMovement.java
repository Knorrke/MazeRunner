package org.mazerunner.model.creature.movements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.mazerunner.model.creature.MapNode;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;

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
    Map<MapNode, MapNode> previous = new HashMap<>();
    Map<MapNode, Integer> distances = new HashMap<>();
    MapNode start = new MapNode(currentX, currentY);
    MapNode found = findUnknown(start, previous, distances, visited);
    if (found != null && previous.containsValue(start)) {
      MapNode goal = found;
      while (previous.get(goal) != start) {
        goal = previous.get(goal);
      }
      LOG.finest(
          String.format("Moving towards unknown field, next stop %d,%d", goal.getX(), goal.getY()));
      return new double[] {goal.getX() + currentX % 1, goal.getY() + currentY % 1};
    } else {
      LOG.warning("No unknown fields! This means that there is no path to the goal.");
      return new double[] {currentX + 1, currentY};
    }
  }

  private MapNode findUnknown(
      MapNode start,
      Map<MapNode, MapNode> previous,
      Map<MapNode, Integer> distances,
      VisitedMap visited) {
    List<MapNode> closed = new ArrayList<>();
    List<MapNode> next = new ArrayList<>();
    distances.put(start, 0);
    next.add(start);
    while (!next.isEmpty()) {
      MapNode nextEl = removeClosestUnvisited(next, distances);
      closed.add(nextEl);

      for (MapNode neighbor : nextEl.getNeighbors()) {
        if (closed.contains(neighbor)) continue;
        if (visited.isWall(neighbor.getX(), neighbor.getY())) {
          closed.add(neighbor);
          continue;
        }
        int newDistance = distances.getOrDefault(nextEl, Integer.MIN_VALUE) + 1;
        int oldDistance = distances.getOrDefault(neighbor, Integer.MAX_VALUE);
        if (newDistance < oldDistance) {
          distances.put(neighbor, newDistance);
          previous.put(neighbor, nextEl);
        }

        if (visited.isUnknown(neighbor.getX(), neighbor.getY())) {
          LOG.finest(
              String.format("Found UNKNOWN field at %d,%d", neighbor.getX(), neighbor.getY()));
          return neighbor;
        }
        if (!next.contains(neighbor)) next.add(neighbor);
      }
    }
    return null;
  }

  private MapNode removeClosestUnvisited(List<MapNode> next, Map<MapNode, Integer> dist) {
    Collections.sort(
        next,
        (n1, n2) -> {
          int dist1 = dist.containsKey(n1) ? dist.get(n1) : Integer.MAX_VALUE;
          int dist2 = dist.containsKey(n2) ? dist.get(n2) : Integer.MAX_VALUE;
          return dist1 - dist2;
        });
    return next.remove(0);
  }
}
