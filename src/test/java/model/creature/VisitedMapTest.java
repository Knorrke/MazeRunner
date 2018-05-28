package model.creature;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import application.model.creature.VisitedMap;

public class VisitedMapTest {
  private int maxX, maxY;
  private VisitedMap visitedMap;

  @Before
  public void setUp() {
    maxX = 5;
    maxY = 5;
    visitedMap = new VisitedMap(maxX, maxY);
  }

  @Test
  public void mergeOnlyUselessTest() {
    int[][] useless1 = {{0, 4}, {0, 3}, {0, 2}};
    int[][] useless2 = {{4, 4}, {4, 3}, {4, 2}};
    for (int[] uselessPos : useless1) {
      visitedMap.markUseless(uselessPos[0], uselessPos[1]);
    }
    VisitedMap visitedMap2 = new VisitedMap(maxX, maxY);
    for (int[] uselessPos : useless2) {
      visitedMap2.markUseless(uselessPos[0], uselessPos[1]);
    }
    assertNotEquals("Visited maps shouln't be equal before merge", visitedMap, visitedMap2);
    VisitedMap.merge(visitedMap, visitedMap2);
    assertNotSame(
        "Visited maps containing only USELESS markings shouln't be the same object...",
        visitedMap,
        visitedMap2);
    assertEquals("... but should be equal after merge", visitedMap, visitedMap2);
  }

  @Test
  public void mergeUselessWithWallsTest() {
    int[][] walls1 = {{0, 4}, {0, 3}, {0, 2}};
    int[][] walls2 = {{4, 4}, {4, 3}, {4, 2}};
    for (int[] wall : walls1) {
      visitedMap.markWall(wall[0], wall[1]);
    }
    VisitedMap visitedMap2 = new VisitedMap(maxX, maxY);
    for (int[] wall : walls2) {
      visitedMap2.markWall(wall[0], wall[1]);
    }
    assertNotEquals("Visited maps shouln't be equal before merge", visitedMap, visitedMap2);
    VisitedMap.merge(visitedMap, visitedMap2);
    assertNotSame(
        "Visited maps containing only USELESS markings shouln't be the same object...",
        visitedMap,
        visitedMap2);
    assertEquals("... but should be equal after merge", visitedMap, visitedMap2);
  }

  @Test
  public void markLastMovesUselessTest() {
    visitedMap = new VisitedMap(maxX, maxY);
    // open 0,2 and 2,4 and path through y=3
    for (int i = 0; i < maxX; i++) {
      if (i != 0) {
        visitedMap.markWall(i, 2);
      }
      if (i != 2) {
        visitedMap.markWall(i, 4);
      }
    }
    visitedMap.markWall(maxX - 1, 3);

    // simulate walk through path y=3
    for (int i = 0; i < maxX - 1; i++) {
      visitedMap.visit(i, 3);
      assertTrue(visitedMap.isVisited(i, 3));
    }
    assertEquals(maxX - 1, visitedMap.getLastVisited().size());

    visitedMap.backtrackToUnknown();
    for (int i = 3; i < maxX - 1; i++) {
      assertTrue(
          "backtrack should mark visited positions as useless until next to unknown field",
          visitedMap.isUseless(i, 3));
    }
    assertFalse(
        "backtrack should not mark 2,3 as useless (because 2,4 is unknown)",
        visitedMap.isUseless(2, 3));
    assertEquals(3, visitedMap.getLastVisited().size());

    visitedMap.backtrackToUnknown();
    assertEquals(
        "backtrack should have no effect if map wasn't changed",
        3,
        visitedMap.getLastVisited().size());
    assertFalse(
        "backtrack should not mark 2,3 as useless (because 2,4 is unknown)",
        visitedMap.isUseless(2, 3));

    for (int i = maxX - 2; i > 2; i--) {
      visitedMap.visit(i, 3);
    }
    assertEquals(
        "backtrack should have no effect because visited fields were already useless",
        3,
        visitedMap.getLastVisited().size());
    visitedMap.backtrackToUnknown();
    assertEquals(
        "backtrack should have no effect because visited fields were already useless",
        3,
        visitedMap.getLastVisited().size());
    assertFalse(
        "backtrack should not mark 2,3 as useless (because 2,4 is unknown)",
        visitedMap.isUseless(2, 3));
  }
}
