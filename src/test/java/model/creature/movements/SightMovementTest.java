package model.creature.movements;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.SightMovement;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.model.maze.Maze;

public class SightMovementTest {
  private int maxX = 10, maxY = 10;
  private int x, y;
  private MovementInterface movement;
  private VisitedMap visited;
  private Vision vision;
  private Maze maze;

  @Before
  public void setup() {
    movement = new SightMovement();
    visited = new VisitedMap(maxX, maxY);
    maze = new Maze(maxX, maxY);
    x = 2;
    y = 3;
    vision = new Vision(Integer.MAX_VALUE, maze);
  }

  @Test
  public void dontMoveIntoBlindAlley() {
    for (int i = 0; i <= 3; i++) {
      maze.buildWall(x + i, y + 1);
      maze.buildWall(x + i, y - 1);
    }
    maze.buildWall(x + 3, y);
    double[] nextPos = movement.getNextGoal(vision, visited, x + 0.5, y + 0.5);
    assertTrue("should have seen wall", visited.isWall(x + 3, y));
    assertArrayEquals(
        "Creature should move out of blind alley",
        new double[] {x + 0.5 - 1, y + 0.5},
        nextPos,
        0.1);
  }

  @Test
  public void moveTowardsGapInWalls() {
    for (int i = -1; i < 4; i++) {
      if (i != 1) {
        maze.buildWall(x + i, y + 1);
      }
      maze.buildWall(x + i, y - 1);
    }
    maze.buildWall(x + 4, y);
    for (int i = 1; i <= 2; i++) {
      maze.buildWall(x, y + i);
      maze.buildWall(x + 2, y + i);
    }
    maze.buildWall(x + 2, y + 3);
    double cx = x + 0.2;
    double cy = y + 0.2;
    double[] nextPos = movement.getNextGoal(vision, visited, cx, cy);
    assertArrayEquals("Creature should move towards gap", new double[] {cx + 1, cy}, nextPos, 0.1);
    nextPos = movement.getNextGoal(vision, visited, cx + 1, cy);
    assertArrayEquals(
        "Creature should turn around at the crossing", new double[] {cx, cy}, nextPos, 0.1);
  }
}
