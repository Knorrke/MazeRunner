package model.maze;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mazerunner.model.maze.GraphSolver;
import org.mazerunner.model.maze.MazeNode;

public class GraphSolverTest {

  @Test
  public void generatesShortestPaths() {
    int[][] map = {
      {0, 0, 1, 0, 0},
      {0, 1, 0, 0, 1},
      {0, 0, 0, 0, 0},
      {1, 0, 1, 0, 0},
      {0, 0, 0, 1, 0},
    };
    int[][] expectedDistances = {
      {8, 9, -1, 1, 0},
      {7, -1, 3, 2, -1},
      {6, 5, 4, 3, 4},
      {-1, 6, -1, 4, 5},
      {8, 7, 8, -1, 6},
    };
    var paths =
        GraphSolver.calculateShortestPaths(
            new MazeNode(4, 0),
            (x, y) -> (x < 0 || y < 0 || y >= map.length || x >= map[y].length || map[y][x] == 1),
            (node, distance) -> {
              assertEquals(
                  "Node " + node.getX() + "," + node.getY() + " should have the expected distance",
                  expectedDistances[node.getY()][node.getX()],
                  distance.intValue());
            });
    assertEquals(
        "Node (1,0) has (0,0) as path information",
        new MazeNode(0, 0),
        paths.get(new MazeNode(1, 0)));
    assertEquals(
        "Node (0,4) has (1,4) as path information",
        new MazeNode(1, 4),
        paths.get(new MazeNode(0, 4)));
    assertEquals(
        "Node (1,4) has (1,3) as path information",
        new MazeNode(1, 3),
        paths.get(new MazeNode(1, 4)));
  }
}
