package org.mazerunner.util;

import java.util.Arrays;
import java.util.List;

public class MazeNode extends MapNode {

  public MazeNode(double x, double y) {
    super(x, y);
  }

  public MazeNode(int x, int y) {
    super(x, y);
  }

  @Override
  public List<MapNode> getNeighbors() {
    {
      MapNode left = new MazeNode(x - 1, y);
      MapNode right = new MazeNode(x + 1, y);
      MapNode up = new MazeNode(x, y - 1);
      MapNode down = new MazeNode(x, y + 1);

      return Arrays.asList(left, right, up, down);
    }
  }
}
