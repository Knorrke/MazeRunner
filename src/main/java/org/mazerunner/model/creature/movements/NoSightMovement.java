package org.mazerunner.model.creature.movements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;

public class NoSightMovement implements MovementInterface {
  private static final Logger LOG = Logger.getLogger(NoSightMovement.class.getName());

  @Override
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY) {
    double[][] directions = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}};
    for (double[] direction : directions) {
      int x = (int) (currentX + direction[0]);
      int y = (int) (currentY + direction[1]);
      if (x >= 0 && y >= 0 && visited.isUnknown(x, y)) {
        return new double[] {currentX + direction[0], currentY + direction[1]};
      }
    }
    return findUnknown(vision, visited, currentX, currentY);
  }

  private double[] findUnknown(
      Vision vision, VisitedMap visited, double currentX, double currentY) {
    Map<Node, Node> previous = new HashMap<>();
    Map<Node, Integer> dist = new HashMap<>();
    List<Node> closed = new ArrayList<>();
    List<Node> next = new ArrayList<>();
    Node start = new Node(currentX, currentY);
    dist.put(start, 0);
    next.add(start);
    Node found = null;
    while (found == null && !next.isEmpty()) {
      Collections.sort(
          next,
          (n1, n2) -> {
            int dist1 = dist.containsKey(n1) ? dist.get(n1) : Integer.MAX_VALUE;
            int dist2 = dist.containsKey(n2) ? dist.get(n2) : Integer.MAX_VALUE;
            return dist1 - dist2;
          });
      Node nextEl = next.remove(0);
      closed.add(nextEl);
      Node left = new Node(nextEl.x - 1, nextEl.y);
      Node right = new Node(nextEl.x + 1, nextEl.y);
      Node up = new Node(nextEl.x, nextEl.y - 1);
      Node down = new Node(nextEl.x, nextEl.y + 1);
      for (Node neighbor : Arrays.asList(left, right, up, down)) {
        if (closed.contains(neighbor)) continue;
        if (visited.isWall(neighbor.x, neighbor.y)) {
          closed.add(neighbor);
          continue;
        } else {
          if (!next.contains(neighbor)) next.add(neighbor);

          if (dist.containsKey(nextEl)
              && (!dist.containsKey(neighbor) || dist.get(nextEl) + 1 < dist.get(neighbor))) {
            dist.put(neighbor, dist.get(nextEl) + 1);
            previous.put(neighbor, nextEl);
          }

          if (visited.isUnknown(neighbor.x, neighbor.y)) {
            LOG.finest(String.format("Found UNKNOWN field at %d,%d", neighbor.x, neighbor.y));
            found = neighbor;
            break;
          }
        }
      }
    }

    if (found != null && previous.containsValue(start)) {
      Node goal = found;
      while (previous.get(goal) != start) {
        goal = previous.get(goal);
      }
      LOG.finest(String.format("Moving towards unknown field, next stop %d,%d", goal.x, goal.y));
      return new double[] {goal.x + currentX % 1, goal.y + currentY % 1};
    } else {
      LOG.warning("No unknown fields! This means that there is no path to the goal.");
      return new double[] {currentX + 1, currentY};
    }
  }

  private class Node {
    public int x, y;

    public Node(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Node(double x, double y) {
      this((int) x, (int) y);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Node)) {
        return false;
      } else {
        Node other = (Node) obj;
        return x == other.x && y == other.y;
      }
    }
  }
}
