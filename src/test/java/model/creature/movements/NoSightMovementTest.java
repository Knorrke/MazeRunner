package model.creature.movements;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.NoSightMovement;

public class NoSightMovementTest {
  private int maxX = 5;
  private int maxY = 5;
  private int x;
  private int y;
  private MovementInterface movement;
  private VisitedMap visited;

  @Before
  public void setup() {
    movement = new NoSightMovement();
    visited = new VisitedMap(maxX, maxY);
    x = 2;
    y = 3;
    visited.markWall(x + 1, y); // in front (in x)
    visited.markWall(x, y + 1); // above
    visited.markWall(x, y - 1); // below
  }

  @Test
  public void moveForwardIfEmpty() {
    double[] nextPos = movement.getNextGoal(null, visited, 0, y);
    assertArrayEquals(
        "Creature should move forward (in x direction)", new double[] {1, y}, nextPos, 0.1);
  }

  @Test
  public void moveOutOfBlindAllay() {
    double[] nextPos = movement.getNextGoal(null, visited, x, y);
    assertArrayEquals(
        "Creature should move out of blind alley", new double[] {x - 1, y}, nextPos, 0.1);
  }

  @Test
  public void avoidVisitedSquares() {
    visited.markVisited(x, y);
    double[] nextPos = movement.getNextGoal(null, visited, x - 1, y);
    assertFalse("Should not move to useless square", Arrays.equals(new double[] {x, y}, nextPos));
  }

  @Test
  public void backtrackingTest() {
    visited.visit(x - 1, y);
    visited.visit(x, y);
    double[] nextPos = movement.getNextGoal(null, visited, x, y);
    assertArrayEquals("should backtrack correctly", new double[] {x - 1, y}, nextPos, 0.1);
    nextPos = movement.getNextGoal(null, visited, x - 1, y);
    assertTrue(
        "should move to the side",
        Arrays.equals(new double[] {x - 1, y + 1}, nextPos)
            || Arrays.equals(new double[] {x - 1, y - 1}, nextPos));
  }

  @Test
  public void backtrackingWithCommunicationTest() {
    visited.visit(x - 1, y);
    visited.visit(x, y);
    // communication information
    visited.markVisited(x - 1, y - 1);
    visited.markVisited(x - 1, y + 1);
    visited.markVisited(x - 2, y);
    // backtracking should move out of useless fields in three moves
    double[] pos = new double[] {x, y};
    for (int i = 0; i <= 2; i++) {
      pos = movement.getNextGoal(null, visited, pos[0], pos[1]);
    }
    assertTrue(
        String.format("position %d,%d should be unknown", (int) pos[0], (int) pos[1]),
        visited.isUnknown((int) pos[0], (int) pos[1]));
  }
}
