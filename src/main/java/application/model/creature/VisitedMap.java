package application.model.creature;

import java.util.Arrays;
import java.util.Stack;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisitedMap {
  public enum VisitedState {
    UNKNOWN,
    VISITED,
    USELESS,
    WALL
  }

  @JsonProperty private VisitedState[][] map;
  private Stack<int[]> lastVisited;

  public VisitedMap() {
    this(0, 0);
  }

  public VisitedMap(int maxX, int maxY) {
    map = new VisitedState[maxX][maxY];
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        map[x][y] = VisitedState.UNKNOWN;
      }
    }
    lastVisited = new Stack<>();
  }

  public void markVisited(int x, int y) {
    if (checkBounds(x, y)) {
      map[x][y] = VisitedState.VISITED;
    }
  }

  public void markUseless(int x, int y) {
    if (checkBounds(x, y)) {
      map[x][y] = VisitedState.USELESS;
    }
  }

  public void markWall(int x, int y) {
    if (checkBounds(x, y)) {
      map[x][y] = VisitedState.WALL;
    }
  }

  public boolean isVisited(int x, int y) {
    if (checkBounds(x, y)) {
      return map[x][y] == VisitedState.VISITED;
    } else {
      return true;
    }
  }

  public boolean isUseless(int x, int y) {
    if (checkBounds(x, y)) {
      return map[x][y] == VisitedState.USELESS;
    } else {
      return true;
    }
  }

  public boolean isUnknown(int x, int y) {
    if (checkBounds(x, y)) {
      return map[x][y] == VisitedState.UNKNOWN;
    } else {
      return false;
    }
  }

  public boolean isWall(int x, int y) {
    if (checkBounds(x, y)) {
      return map[x][y] == VisitedState.WALL;
    } else {
      return true;
    }
  }

  /**
   * Merge the {@link VisitedState#USELESS} markings of two maps
   *
   * @param visitedMap1
   * @param visitedMap2
   */
  public static void mergeUseless(VisitedMap visitedMap1, VisitedMap visitedMap2) {
    visitedMap1.mergeUseless(visitedMap2);
  }

  /**
   * Method for merging the {@link VisitedState#USELESS} markings with another map. This method is
   * private, use {@link #mergeUseless(VisitedMap, VisitedMap) static mergeUseless} method instead.
   *
   * @param visitedMap2
   */
  private void mergeUseless(VisitedMap visitedMap2) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        if (this.isUseless(x, y)) visitedMap2.markUseless(x, y);
        else if (visitedMap2.isUseless(x, y)) this.markUseless(x, y);
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof VisitedMap)) return false;

    VisitedMap visitedMap2 = (VisitedMap) obj;
    return Arrays.deepEquals(map, visitedMap2.map);
  }

  /**
   * Marks the field as visited and adds it to the lastVisited Stack
   *
   * @param x
   * @param y
   */
  public void visit(int x, int y) {
    if (isUnknown(x, y)) {
      markVisited(x, y);
      lastVisited.add(new int[] {x, y});
    }
  }

  public Stack<int[]> getLastVisited() {
    return lastVisited;
  }

  /**
   * Backtracks {@link VisitedMap#lastVisited lastVisited} and marks them as useless, until there's
   * an unknown field next to it.
   */
  public void backtrackToUnknown() {
    while (!lastVisited.isEmpty()) {
      int[] visited = lastVisited.peek();
      int x = visited[0];
      int y = visited[1];
      if (isUnknown(x, y + 1)
          || isUnknown(x, y - 1)
          || isUnknown(x + 1, y)
          || isUnknown(x - 1, y)) {
        break;
      } else {
        markUseless(x, y);
        lastVisited.pop();
      }
    }
  }

  /**
   * Check if the coordinates are valid
   *
   * @param x
   * @param y
   * @return true if and only if x >= 0 && x < maxWallX && y >= 0 && y <= maxWallY
   */
  public boolean checkBounds(int x, int y) {
    return x >= 0 && x < map.length && y >= 0 && y < map[x].length;
  }
}
