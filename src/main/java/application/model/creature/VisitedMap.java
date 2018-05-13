package application.model.creature;

import java.util.Arrays;

public class VisitedMap {
  public enum VisitedState {
    UNKNOWN,
    VISITED,
    USELESS
  }

  private VisitedState[][] map;

  public VisitedMap(int maxX, int maxY) {
    map = new VisitedState[maxX][maxY];
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        map[x][y] = VisitedState.UNKNOWN;
      }
    }
  }

  public void markVisited(int x, int y) {
    if (x < map.length && y < map[x].length) {
      map[x][y] = VisitedState.VISITED;
    }
  }

  public void markUseless(int x, int y) {
    if (x < map.length && y < map[x].length) {
      map[x][y] = VisitedState.USELESS;
    }
  }

  public boolean isVisited(int x, int y) {
    if (x < map.length && y < map[x].length) {
      return map[x][y] == VisitedState.VISITED;
    } else {
      return true;
    }
  }

  public boolean isUseless(int x, int y) {
    if (x < map.length && y < map[x].length) {
      return map[x][y] == VisitedState.USELESS;
    } else {
      return true;
    }
  }

  public boolean isUnknown(int x, int y) {
    if (x < map.length && y < map[x].length) {
      return map[x][y] == VisitedState.UNKNOWN;
    } else {
      return false;
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
}
