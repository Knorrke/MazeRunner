package model.creature.movements;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import application.model.creature.VisitedMap;
import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.creature.vision.Vision;
import application.model.maze.MazeModelInterface;

public class NoSightMovementTest {
  int maxX = 5, maxY = 5;
  double x, y;
  MazeModelInterface maze;
  MovementInterface movement;
  Vision vision;
  VisitedMap visited;

  @Before
  public void setup() {
    movement = new NoSightMovement();
    vision = new Vision();
    visited = new VisitedMap(maxX, maxY);
    x = 2;
    y = 3;
    visited.markWall((int) x + 1, (int) y); // in front (in x)
    visited.markWall((int) x, (int) y + 1); // above
    visited.markWall((int) x, (int) y - 1); // below
  }

  @Test
  public void moveForwardIfEmpty() {
    double[] nextPos = movement.getNextGoal(vision, visited, 0, y);
    assertArrayEquals(
        "Creature should move forward (in x direction)", new double[] {1, y}, nextPos, 0.1);
  }

  @Test
  public void moveOutOfBlindAllay() {
    double[] nextPos = movement.getNextGoal(vision, visited, x, y);
    assertArrayEquals(
        "Creature should move out of blind alley", new double[] {x - 1, y}, nextPos, 0.1);
  }

  @Test
  public void avoidUselessSquares() {
    visited.markUseless((int) x, (int) y);
    double[] nextPos = movement.getNextGoal(vision, visited, x - 1, y);
    assertFalse("Should not move to useless square", Arrays.equals(new double[] {x, y}, nextPos));
  }

  @Test
  public void backtrackingTest() {
    visited.visit((int) x - 1, (int) y);
    visited.visit((int) x, (int) y);
    double[] nextPos = movement.getNextGoal(vision, visited, x, y);
    assertArrayEquals("should backtrack correctly", new double[] {x - 1, y}, nextPos, 0.1);
    nextPos = movement.getNextGoal(vision, visited, x - 1, y);
    assertTrue(
        "should move to the side",
        Arrays.equals(new double[] {x - 1, y + 1}, nextPos)
            || Arrays.equals(new double[] {x - 1, y - 1}, nextPos));
  }

  @Test
  public void backtrackingWithCommunicationTest() {
    visited.visit((int) x - 1, (int) y);
    visited.visit((int) x, (int) y);
    // communication information
    visited.markUseless((int) x - 1, (int) y - 1);
    visited.markUseless((int) x - 1, (int) y + 1);
    visited.markUseless((int) x - 2, (int) y);
    // backtracking should move out of useless fields in three moves
    double[] pos = new double[] {x, y};
    for (int i = 0; i <= 2; i++) {
      pos = movement.getNextGoal(vision, visited, pos[0], pos[1]);
    }
    assertTrue(
        String.format("position %d,%d should be unknown", (int) pos[0], (int) pos[1]),
        visited.isUnknown((int) pos[0], (int) pos[1]));
  }
}
