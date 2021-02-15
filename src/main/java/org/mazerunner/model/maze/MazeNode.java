package org.mazerunner.model.maze;

import com.sun.scenario.Settings;
import java.util.Arrays;
import java.util.List;

public class MazeNode extends MapNode {

  public MazeNode(double x, double y) {
    super(x, y);
  }

  @Override
  public List<MapNode> getNeighbors() {
    MapNode left = new MazeNode(x - 1, y);
    MapNode right = new MazeNode(x + 1, y);
    MapNode up = new MazeNode(x, y - 1);
    MapNode down = new MazeNode(x, y + 1);

    return Arrays.asList(left, right, up, down);
  }

  @Override
  public boolean isGoal() {
    return x >= Settings.getInt("maxX", 100) - 1;
  }
}
