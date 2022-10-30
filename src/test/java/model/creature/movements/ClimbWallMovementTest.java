package model.creature.movements;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.movements.ClimbWallMovement;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.TowerType;

public class ClimbWallMovementTest {
  private int maxX = 10, maxY = 10;
  private int x, y;
  private double cx, cy;
  private MovementInterface movement;
  private VisitedMap visited;
  private Vision vision;
  private Maze maze;

  @Before
  public void setup() {
    maze = new Maze(maxX, maxY);
    movement = new ClimbWallMovement();
    visited = new VisitedMap(maxX, maxY);
    x = 2;
    y = 3;
    cx = x + 0.5;
    cy = y + 0.5;
    vision = new Vision(Integer.MAX_VALUE, maze);
  }

  @Test
  public void shouldMoveOnWall() {
    maze.buildWall(x, y + 1);
    double[] nextPos = movement.getNextGoal(vision, visited, cx, cy);
    assertArrayEquals("Creature should climb empty wall", new double[] {cx, cy + 1}, nextPos, 0.1);
  }

  @Test
  public void shouldSkipWallWithTower() {
    Wall w = maze.buildWall(x, y + 1);
    maze.buildTower(w, TowerType.NORMAL);
    maze.buildWall(x + 1, y + 1);
    double[] nextPos = movement.getNextGoal(vision, visited, cx, cy);
    assertArrayEquals(
        "Creature should skip wall with tower", new double[] {cx + 1, cy}, nextPos, 0.1);
    nextPos = movement.getNextGoal(vision, visited, cx + 1, cy);
    assertArrayEquals(
        "Then, creature should climb empty wall", new double[] {cx + 1, cy + 1}, nextPos, 0.1);
  }

  @Test
  public void shouldMoveTowardsWall() {
    maze.buildWall(x, y + 2);
    double[] nextPos = movement.getNextGoal(vision, visited, cx, cy);
    assertArrayEquals(
        "Creature should move towards empty wall", new double[] {cx, cy + 1}, nextPos, 0.1);
  }

  @Test
  public void shouldStayOnWall() {
    maze.buildWall(x, y);
    double[] nextPos = movement.getNextGoal(vision, visited, cx, cy);
    assertArrayEquals(
        "Creature should move towards empty wall", new double[] {cx, cy}, nextPos, 0.1);
  }
}
