package org.mazerunner.model.maze;

import java.util.List;

public abstract class MapNode {
  protected int x;
  protected int y;

  public MapNode(int x, int y) {
    this.x = x;
    this.y = y;
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

  public abstract boolean isGoal();

  public abstract List<MapNode> getNeighbors();
}
