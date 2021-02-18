package model.creature.movements;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.movements.RandomMovement;

public class RandomMovementTest {
  private int maxX = 5;
  private int maxY = 5;
  private int x;
  private int y;
  private MovementInterface movement;
  private VisitedMap visited;

  @Before
  public void setup() {
    visited = new VisitedMap(maxX, maxY);
    x = 4;
    y = 2;
    visited.markWall(x + 1, y); // in front (in x)
    visited.markWall(x, y + 1); // above
    visited.markWall(x, y - 1); // below
  }

  @Test
  public void continueInLastDirection() {
    // Using 100% to continue in same direction if possible. Default 0.6
    movement = new RandomMovement(1.0, 0.0);
    double[] firstStep = movement.getNextGoal(null, visited, 2, y);
    double[] firstDirection = {firstStep[0] - 2, firstStep[1] - y};
    double[] secondStep = movement.getNextGoal(null, visited, firstStep[0], firstStep[1]);
    assertArrayEquals(
        "Creature should continue in same direction if not blocked",
        firstDirection,
        new double[] {secondStep[0] - firstStep[0], secondStep[1] - firstStep[1]},
        0.1);
  }

  @Test
  public void oneRandomMovementObjectSeemsRandom() {
    movement = new RandomMovement();
    double[] baseline = movement.getNextGoal(null, visited, 2, y);
    boolean foundDifferent = false;
    for (int i = 0; i < 1000; i++) {
      double[] step = movement.getNextGoal(null, visited, 2, y);
      foundDifferent = foundDifferent || !Arrays.equals(baseline, step);
    }
    assertTrue(
        "Random Movement should result in different movements over a lot of tries", foundDifferent);
  }

  @Test
  public void multipleRandomMovemenObjectsSeemRandom() {
    double[] baseline = null;
    boolean foundDifferent = false;
    for (int i = 0; i < 1000; i++) {
      movement = new RandomMovement();
      double[] step = movement.getNextGoal(null, visited, 2, y);
      if (baseline == null) {
        baseline = step;
      } else {
        foundDifferent = foundDifferent || !Arrays.equals(baseline, step);
      }
    }
    assertTrue(
        "Random Movement should result in different movements over a lot of tries", foundDifferent);
  }

  @Test
  public void shouldOnlyChooseValidOptions() {
    movement = new RandomMovement();
    for (int i = 0; i < 1000; i++) {
      double[] step = movement.getNextGoal(null, visited, x, y);
      assertArrayEquals(
          "Random Movement should only choose valid options", new double[] {x - 1, y}, step, 0.1);
    }
  }

  @Test
  public void defaultToRight() {
    movement = new RandomMovement();
    visited.markWall(x - 1, y); // stuck between walls now.
    double[] step = movement.getNextGoal(null, visited, x, y);
    assertArrayEquals(
        "Creature should move to right anyway, if stuck", new double[] {x + 1, y}, step, 0.1);
  }
}
