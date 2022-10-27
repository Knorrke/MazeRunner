package model.creature.movements;

import static org.junit.Assert.assertArrayEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.PerfectMovement;
import org.mazerunner.model.maze.Maze;

public class PerfectMovementTest {
  private int maxX = 10, maxY = 10;
  private int x, y;
  private MovementInterface movement;
  private VisitedMap visited;
  private Maze maze;

  @Before
  public void setup() {
    visited = new VisitedMap(maxX, maxY);
    maze = new Maze(maxX, maxY);
    movement = new PerfectMovement(maze);
    x = 2;
    y = 3;
  }

  @Test
  public void dontMoveTowardsGapInWallsIfBlocked() {
    // Setup:
    // ++++++
    // c     +
    // ++ +++
    //  + +
    //  +++
    for (int i = -1; i < 4; i++) {
      if (i != 1) {
        maze.buildWall(x + i, y + 1);
      }
      maze.buildWall(x + i, y - 1);
    }
    maze.buildWall(x + 4, y);
    for (int i = 2; i <= 3; i++) {
      maze.buildWall(x, y + i);
      maze.buildWall(x + 2, y + i);
    }
    maze.buildWall(x + 1, y + 4);
    double cx = x + 0.2;
    double cy = y + 0.2;

    double[] nextPos = movement.getNextGoal(null, visited, cx, cy);
    assertArrayEquals(
        "Creature should turn around immediately", new double[] {cx - 1, cy}, nextPos, 0.1);
  }

  @Test
  public void moveToShortestPath() throws JsonProcessingException {
    // Setup:
    // +++++++++
    //
    //  c+++++++
    //   +   +
    //     +   +
    // +++++++++
    for (int i = -1; i < 9; i++) {
      maze.buildWall(x + i, y - 2);
      maze.buildWall(x + i, y + 3);
      if (i > 0) maze.buildWall(x + i, y);
    }
    maze.buildWall(x + 1, y + 1);
    maze.buildWall(x + 3, y + 2);
    maze.buildWall(x + 5, y + 1);
    maze.buildWall(x + 7, y + 2);

    double cx = x + 0.2;
    double cy = y + 0.2;

    double[] nextPos = movement.getNextGoal(null, visited, cx, cy);
    assertArrayEquals(
        "Creature should go up, towards the shorter path", new double[] {cx, cy - 1}, nextPos, 0.1);
  }
}
