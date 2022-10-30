package org.mazerunner.model.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public final class GraphSolver {
  private GraphSolver() {}

  public static Map<MapNode, MapNode> calculateShortestPaths(
      MapNode goal, BiPredicate<Integer, Integer> isClosed) {
    return calculateShortestPaths(goal, isClosed, (_n, _d) -> {});
  }

  public static Map<MapNode, MapNode> calculateShortestPaths(
      MapNode goal, BiPredicate<Integer, Integer> isClosed, Consumer<MapNode> traverse) {
    return calculateShortestPaths(goal, isClosed, (n, _d) -> traverse.accept(n));
  }

  public static Map<MapNode, MapNode> calculateShortestPaths(
      MapNode goal, BiPredicate<Integer, Integer> isClosed, BiConsumer<MapNode, Integer> traverse) {
    Map<MapNode, MapNode> paths = new HashMap<>();
    Map<MapNode, Integer> distances = new HashMap<>();
    List<MapNode> closed = new ArrayList<>();
    List<MapNode> next = new ArrayList<>();
    distances.put(goal, 0);
    next.add(goal);
    while (!next.isEmpty()) {
      MapNode nextEl = removeClosestUnvisited(next, distances);
      closed.add(nextEl);

      traverse.accept(nextEl, distances.get(nextEl));

      for (MapNode neighbor : nextEl.getNeighbors()) {
        if (closed.contains(neighbor)) continue;
        if (distances.containsKey(nextEl)) {
          int newDistance = distances.get(nextEl) + 1;
          int oldDistance = distances.getOrDefault(neighbor, Integer.MAX_VALUE);
          if (newDistance < oldDistance) {
            distances.put(neighbor, newDistance);
            paths.put(neighbor, nextEl);
          }
        }
        if (isClosed.test(neighbor.getX(), neighbor.getY())) {
          closed.add(neighbor);
        } else if (!next.contains(neighbor)) {
          next.add(neighbor);
        }
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
