package org.mazerunner.model.creature.vision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.util.Util;

public class Vision {
  private double radius;
  private MazeModelInterface maze;

  public Vision(double radius, MazeModelInterface maze) {
    this.radius = radius;
    this.maze = maze;
  }

  public void updateVisitedMap(VisitedMap visited, double currentX, double currentY) {
    List<int[]> wallPositions = getWallPositions();
    for (int x = 0; x < maze.getMaxWallX(); x++) {
      for (int y = 0; y < maze.getMaxWallY(); y++) {
        if (visited.isUnknown(x, y)) {
          for (double[] pointToCheck : getCheckpointsFromTopleftCorner(x, y)) {
            if (isVisible(
                pointToCheck[0],
                pointToCheck[1],
                new int[] {x, y},
                currentX,
                currentY,
                wallPositions)) {
              updateInformation(visited, x, y);
              break;
            }
          }
        }
      }
    }
  }

  /**
   * Returns an array with two points for each of the corners of field (x,y), that are close to the
   * corner.
   *
   * @param x
   * @param y
   * @return
   */
  private double[][] getCheckpointsFromTopleftCorner(int x, int y) {
    double inset = 0.01;
    return new double[][] {
      {x + inset, y}, {x, y + inset}, // top left corner
      {x + 1 - inset, y}, {x + 1, y + inset}, // top right corner
      {x + inset, y + 1}, {x, y + 1 - inset}, // bottom left corner
      {x + 1 - inset, y + 1}, {x + 1, y + 1 - inset} // bottom right corner
    };
  }

  private void updateInformation(VisitedMap visited, int x, int y) {
    if (maze.hasWallOn(x, y)) {
      visited.markWall(x, y);
    } else {
      visited.markVisited(x, y);
    }
  }

  private boolean isVisible(
      double x,
      double y,
      int[] checkingField,
      double currentX,
      double currentY,
      List<int[]> wallPositions) {
    if (checkingField[0] == (int) currentX && checkingField[1] == (int) currentY) return true;
    if (Util.distance(x, y, currentX, currentY) > radius) return false;

    ParameterizedLine sight = new ParameterizedLine(currentX, currentY, x - currentX, y - currentY);
    for (int[] wallPosition : wallPositions) {
      if (Arrays.equals(wallPosition, checkingField)) {
        continue;
      }
      List<ParameterizedLine> wallEdges = new ArrayList<>();
      wallEdges.add(new ParameterizedLine(wallPosition[0], wallPosition[1], 1, 0)); // top
      wallEdges.add(new ParameterizedLine(wallPosition[0], wallPosition[1], 0, 1)); // left
      wallEdges.add(new ParameterizedLine(wallPosition[0], wallPosition[1] + 1.0, 1, 0)); // right
      wallEdges.add(new ParameterizedLine(wallPosition[0] + 1.0, wallPosition[1], 0, 1)); // bottom
      for (ParameterizedLine edge : wallEdges) {
        double[] intersection = sight.getIntersection(edge);
        if (intersection != null) {
          return false;
        }
      }
    }
    return true;
  }

  private List<int[]> getWallPositions() {
    List<int[]> wallPositions = new ArrayList<>();
    for (int x = 0; x < maze.getMaxWallX(); x++) {
      for (int y = 0; y < maze.getMaxWallY(); y++) {
        if (maze.hasWallOn(x, y)) {
          wallPositions.add(new int[] {x, y});
        }
      }
    }
    return wallPositions;
  }

  private class ParameterizedLine {
    private double px;
    private double py;
    private double dx;
    private double dy;

    public ParameterizedLine(double px, double py, double dx, double dy) {
      this.px = px;
      this.py = py;
      this.dx = dx;
      this.dy = dy;
    }

    /**
     * Calculates the intersection point to second line
     *
     * @param l2 second line
     * @return array [0]: x, [1]: y, [2]: Parametervalue T
     */
    public double[] getIntersection(ParameterizedLine l2) {
      // check if parallel
      double thisMag = Math.hypot(dx, dy);
      double l2Mag = Math.hypot(l2.dx, l2.dy);
      if (dx / thisMag == l2.dx / l2Mag && dy / thisMag == l2.dy / l2Mag) {
        // Unit vectors are the same.
        return null;
      }

      // Solve as explained on https://ncase.me/sight-and-light/
      double T2 = (dx * (l2.py - py) + dy * (px - l2.px)) / (l2.dx * dy - l2.dy * dx);
      double T1 = (l2.px + l2.dx * T2 - px) / dx;

      // Check parametric bounds
      if (T1 < 0 || T1 > 1) {
        return null;
      }
      if (T2 < 0 || T2 > 1) {
        return null;
      }
      double[] intersection = {px + dx * T1, py + dy * T1, T1};
      return intersection;
    }
  }
}
