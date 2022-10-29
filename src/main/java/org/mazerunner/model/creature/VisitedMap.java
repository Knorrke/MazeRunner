package org.mazerunner.model.creature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Random;

public class VisitedMap {
  private static int[][][] bitStrings;

  public enum VisitedState {
    UNKNOWN,
    VISITED,
    WALL,
    TOWER
  }

  @JsonProperty private VisitedState[][] map;
  @JsonIgnore private boolean changed = true;
  @JsonIgnore private int hash = 0;

  public VisitedMap() {
    this(0, 0);
  }

  public VisitedMap(int maxX, int maxY) {
    initializeBitStrings(maxX, maxY);
    map = new VisitedState[maxX][maxY];
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        map[x][y] = VisitedState.UNKNOWN;
        hash ^= bitStrings[x][y][VisitedState.UNKNOWN.ordinal()];
      }
    }
  }

  private void initializeBitStrings(int maxX, int maxY) {
    if (bitStrings == null) {
      bitStrings = new int[maxX][maxY][VisitedState.values().length];
      for (int x = 0; x < maxX; x++) {
        for (int y = 0; y < maxY; y++) {
          for (int j = 0; j < VisitedState.values().length; j++) {
            bitStrings[x][y][j] = new Random().nextInt();
          }
        }
      }
    }
  }

  public void markVisited(int x, int y) {
    if (checkBounds(x, y) && map[x][y] != VisitedState.VISITED) {
      setNewStateOnPosition(x, y, VisitedState.VISITED);
    }
  }

  public void markWall(int x, int y) {
    if (checkBounds(x, y) && map[x][y] != VisitedState.WALL) {
      setNewStateOnPosition(x, y, VisitedState.WALL);
    }
  }

  public void markTower(int x, int y) {
    if (checkBounds(x, y) && map[x][y] != VisitedState.TOWER) {
      setNewStateOnPosition(x, y, VisitedState.TOWER);
    }
  }

  private void setNewStateOnPosition(int x, int y, VisitedState newState) {
    VisitedState old = map[x][y];
    hash ^= bitStrings[x][y][old.ordinal()] ^ bitStrings[x][y][newState.ordinal()];
    map[x][y] = newState;
  }

  @Override
  public int hashCode() {
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
      return map[x][y] == VisitedState.WALL || map[x][y] == VisitedState.TOWER;
    } else {
      return true;
    }
  }

  public boolean isTower(Integer x, Integer y) {
    if (checkBounds(x, y)) {
      return map[x][y] == VisitedState.TOWER;
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
