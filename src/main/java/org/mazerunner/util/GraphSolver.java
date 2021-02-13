package org.mazerunner.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class GraphSolver {

  public static Map<MapNode, MapNode> calculatePerfectMoveMap(
      MapNode goal, BiPredicate<Integer, Integer> isClosed) {
    return calculatePerfectMoveMap(goal, isClosed, ignore -> {});
  }

  public static Map<MapNode, MapNode> calculatePerfectMoveMap(
      MapNode goal, BiPredicate<Integer, Integer> isClosed, Consumer<MapNode> traverse) {
    Map<MapNode, MapNode> paths = new HashMap<>();
    Map<MapNode, Integer> distances = new HashMap<>();
    List<MapNode> closed = new ArrayList<>();
    List<MapNode> next = new ArrayList<>();
    distances.put(goal, 0);
    next.add(goal);
    while (!next.isEmpty()) {
      MapNode nextEl = removeClosestUnvisited(next, distances);
      closed.add(nextEl);
      for (MapNode neighbor : nextEl.getNeighbors()) {
        if (closed.contains(neighbor)) continue;
        if (isClosed.test(neighbor.getX(), neighbor.getY())) {
          closed.add(neighbor);
          continue;
        }
        if (distances.containsKey(nextEl)) {
          int newDistance = distances.get(nextEl) + 1;
          int oldDistance = distances.getOrDefault(neighbor, Integer.MAX_VALUE);
          if (newDistance < oldDistance) {
            distances.put(neighbor, newDistance);
            paths.put(neighbor, nextEl);
          }
        }

        traverse.accept(neighbor);

        if (!next.contains(neighbor)) next.add(neighbor);
      }
    }
    return paths;
  }

  private static MapNode removeClosestUnvisited(List<MapNode> next, Map<MapNode, Integer> dist) {
    Collections.sort(
        next,
        (n1, n2) -> {
          int dist1 = dist.getOrDefault(n1, Integer.MAX_VALUE);
          int dist2 = dist.getOrDefault(n2, Integer.MAX_VALUE);
          return dist1 - dist2;
        });
    return next.remove(0);
  }
}
