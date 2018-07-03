package org.mazerunner.model.creature;

import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VisitedMap {
  public enum VisitedState {
    UNKNOWN,
    VISITED,
    WALL
  }

  @JsonProperty private VisitedState[][] map;
  @JsonIgnore private boolean changed = true;
  @JsonIgnore private int hash = 0;

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
  }

  public void markVisited(int x, int y) {
    if (checkBounds(x, y) && map[x][y] != VisitedState.VISITED) {
      map[x][y] = VisitedState.VISITED;
      changed = true;
    }
  }

  public void markWall(int x, int y) {
    if (checkBounds(x, y) && map[x][y] != VisitedState.WALL) {
      map[x][y] = VisitedState.WALL;
      changed = true;
    }
  }

  @Override
  public int hashCode() {
    if (changed) {
      hash = Arrays.deepHashCode(map);
    }
    return hash;
  }

  public boolean isVisited(int x, int y) {
    if (checkBounds(x, y)) {
      return map[x][y] == VisitedState.VISITED;
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
   * Merge the {@link VisitedState#USELESS} and {@link VisitedState#WALL} markings of two maps
   *
   * @param visitedMap1
   * @param visitedMap2
   * @return number of changes
   */
  public static int merge(VisitedMap visitedMap1, VisitedMap visitedMap2) {
    return visitedMap1.merge(visitedMap2);
  }

  /**
   * Method for merging the {@link VisitedState#USELESS} and {@link VisitedState#WALL} markings with
   * another map. This method is private, use {@link #merge(VisitedMap, VisitedMap) static merge}
   * method instead.
   *
   * @param visitedMap2
   * @return number of changes
   */
  private int merge(VisitedMap visitedMap2) {
    int numberOfChanges = 0;
    if (hashCode() != visitedMap2.hashCode()) {
      for (int x = 0; x < map.length; x++) {
        for (int y = 0; y < map[x].length; y++) {
          if (this.isWall(x, y) && !visitedMap2.isWall(x, y)) {
            visitedMap2.markWall(x, y);
            numberOfChanges++;
          } else if (visitedMap2.isWall(x, y) && !this.isWall(x, y)) {
            this.markWall(x, y);
            numberOfChanges++;
          } else if (this.isVisited(x, y) && !visitedMap2.isVisited(x, y)) {
            visitedMap2.markVisited(x, y);
            numberOfChanges++;
          } else if (visitedMap2.isVisited(x, y) && !this.isVisited(x, y)) {
            this.markVisited(x, y);
            numberOfChanges++;
          }
        }
      }
    }
    return numberOfChanges;
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
