package model.creature;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;

public class CreatureTest {

  Creature creature;
  MazeModelInterface maze;
  double startX, startY;

  @Before
  public void setup() {
    maze = new Maze();
    creature = CreatureFactory.create(maze, CreatureType.NORMAL, 0, 0);
    maze.addCreature(creature);
    startX = creature.getX();
    startY = creature.getY();
  }

  @Test
  public void creatureShouldMove() {
    moveOneFieldAutonomously(creature);
    assertTrue(
        "Creature should move on moveOneFieldAutonomously(call of)",
        isMoved(creature, startX, startY));
  }

  @Test
  public void shouldMoveForwardOnEmptyMaze() {
    assertEquals("Maze should have no walls", 0, maze.getWalls().size());
    moveOneFieldAutonomously(creature);
    assertTrue(
        "Creature should move forward (in x direction)",
        movedTo(creature, startX + 1, startY, 0.1));
  }

  @Test
  public void shouldUpdateVisitedOnMove() {
    moveOneFieldAutonomously(creature);
    assertTrue(
        "Start field should be visited",
        creature.getVisitedMap().isVisited((int) startX, (int) startY));
    assertFalse(
        "Start position and new position shouldn't match",
        (int) startX == (int) creature.getX() && (int) startY == (int) creature.getY());
    assertTrue(
        "Current field should be visited",
        creature.getVisitedMap().isVisited((int) creature.getX(), (int) creature.getY()));
  }

  @Test
  public void shouldUpdateUselessInBlindAlley() {
    int x = (int) startX + 1, y = (int) startY;
    buildHorizontalBlindAlley(x, y, 1);
    creature.moveBy(1, 0); // move to x,y (blind alley)
    moveOneFieldAutonomously(creature);
    assertTrue("BlindAlley should be marked useless", creature.getVisitedMap().isUseless(x, y));
  }

  @Test
  public void shouldNotMoveOutOfBoard() {
    int y1 = 5;
    Creature creature1 = CreatureFactory.create(maze, CreatureType.NORMAL, 0, y1 - 1);
    // starting on 0,y1 + 1 move in a circle to visit all fields surrounding 0,y1
    creature1.moveBy(1, 0);
    creature1.moveBy(0, 1);
    creature1.moveBy(0, 1);
    creature1.moveBy(-1, 0);
    creature1.moveBy(0, -1);
    assertTrue("creature is on 0,y1 now", movedTo(creature1, 0, y1, 0.1));

    moveOneFieldAutonomously(creature1);
    assertTrue("Creature should have moved", isMoved(creature1, 0, y1));
    assertTrue(
        "Creature should not have moved from board",
        creature1.getX() >= 0 && creature1.getY() >= 0);
  }

  @Test
  public void meetingCreatureShouldUpdateBlindAllayes() {
    // Move creatures a bit in blind alleys
    int fieldsBetweenCreatures = 2, blindAlleyLength = 3;
    int x1 = (int) startX, y1 = (int) startY;
    int x2 = (int) startX, y2 = (int) startY + 2 * fieldsBetweenCreatures;
    Creature creature2 = CreatureFactory.create(maze, CreatureType.NORMAL, x2, y2);
    maze.addCreature(creature2);
    buildHorizontalBlindAlley(x1 + 1, y1, blindAlleyLength); // in front of creature1
    buildHorizontalBlindAlley(x2 + 1, y2, blindAlleyLength); // in front of creature2
    for (int i = 0; i < 2 * blindAlleyLength; i++) {
      moveOneFieldAutonomously(creature);
      moveOneFieldAutonomously(creature2);
    }
    assertTrue(movedTo(creature, x1, y1, 0.01));
    assertTrue(movedTo(creature2, x2, y2, 0.01));

    assertTrue(
        "creatures should have different maps", creature.getVisitedMap().isUseless(x1 + 1, y1));
    assertFalse(creature2.getVisitedMap().isUseless(x1 + 1, y1));
    assertTrue(creature2.getVisitedMap().isUseless(x2 + 1, y2));
    assertFalse(creature.getVisitedMap().isUseless(x2 + 1, y2));

    for (int i = 0; i < fieldsBetweenCreatures; i++) {
      creature.moveBy(0, 1);
      creature2.moveBy(0, -1);
    }
    assertTrue(
        "creatures should be on same square now",
        movedTo(creature, creature2.getX(), creature2.getY(), 0.5));

    assertTrue(
        "creatures should have synchronized USELESS information",
        creature.getVisitedMap().isUseless(x1 + 1, y1));
    assertTrue(creature2.getVisitedMap().isUseless(x1 + 1, y1));
    assertTrue(creature2.getVisitedMap().isUseless(x2 + 1, y1));
    assertTrue(creature.getVisitedMap().isUseless(x2 + 1, y2));
  }

  @Test
  public void backtrackTest() {
    int x = (int) startX;
    int y = (int) startY;
    int length = 3;
    buildHorizontalBlindAlley(x + 1, y, length);
    buildVerticalBlindAlley(x, y + 1, length);
    maze.buildWall(x - 1, y);
    maze.buildWall(x, y - 1);
    for (int i = 0; i < length; i++) {
      creature.moveBy(1, 0);
    }
    for (int i = 0; i < length; i++) {
      moveOneFieldAutonomously(creature);
    }
    assertTrue("back to start", movedTo(creature, x, y, 0.1));
    moveOneFieldAutonomously(creature);
    assertTrue("moved to unknown", movedTo(creature, x, y + 1, 0.1));
    moveOneFieldAutonomously(creature);
    assertTrue("moved to unknown", movedTo(creature, x, y + 2, 0.1));
  }

  @Test
  public void dieOnDamage() {
    creature.damage(creature.getLifes() + 1);
    assertFalse(maze.getCreatures().contains(creature));
  }

  /** Helper functions */
  private void moveOneFieldAutonomously(Creature c) {
    c.chooseNewAction();
    c.act(1 / c.getVelocity());
  }

  private void buildHorizontalBlindAlley(int x, int y, int length) {
    for (int i = 0; i < length; i++) {
      maze.buildWall(x + i, y + 1); // above
      maze.buildWall(x + i, y - 1); // below
    }
    maze.buildWall(x + length, y); // in front of
  }

  private void buildVerticalBlindAlley(int x, int y, int length) {
    for (int i = 0; i < length; i++) {
      maze.buildWall(x + 1, y + i); // right
      maze.buildWall(x - 1, y + i); // left
    }
    maze.buildWall(x, y + length); // in front of
  }

  private static boolean isMoved(Creature c, double oldX, double oldY) {
    return !movedTo(c, oldX, oldY, 0.1);
  }

  private static boolean movedTo(Creature c, double newX, double newY, double precision) {
    return newX > c.getX() - precision
        && newX < c.getX() + precision
        && newY > c.getY() - precision
        && newY < c.getY() + precision;
  }
}
