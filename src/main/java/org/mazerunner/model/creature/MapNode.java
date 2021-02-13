package org.mazerunner.model.creature;

import java.util.Arrays;
import java.util.List;

public class MapNode {
  private int x;
  private int y;

  public MapNode(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public MapNode(double x, double y) {
    this((int) x, (int) y);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MapNode)) {
      return false;
    } else {
      MapNode other = (MapNode) obj;
      return x == other.getX() && y == other.getY();
    }
  }

  @Override
  public int hashCode() {
    int hash = 23;
    hash = hash * 37 + x;
    hash = hash * 37 + y;
    return hash;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public List<MapNode> getNeighbors() {
    MapNode left = new MapNode(x - 1, y);
    MapNode right = new MapNode(x + 1, y);
    MapNode up = new MapNode(x, y - 1);
    MapNode down = new MapNode(x, y + 1);

    return Arrays.asList(left, right, up, down);
  }
}
