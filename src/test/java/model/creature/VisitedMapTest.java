package model.creature;

import static org.junit.Assert.*;

import org.junit.Test;

import application.model.creature.VisitedMap;

public class VisitedMapTest {

  @Test
  public void mergeOnlyUselessTest() {
    int maxX = 5, maxY = 5;
    int[][] useless1 = {{0, 4}, {0, 3}, {0, 2}};
    int[][] useless2 = {{4, 4}, {4, 3}, {4, 2}};
    VisitedMap visitedMap1 = new VisitedMap(maxX, maxY);
    for (int[] uselessPos : useless1) {
      visitedMap1.markUseless(uselessPos[0], uselessPos[1]);
    }
    VisitedMap visitedMap2 = new VisitedMap(maxX, maxY);
    for (int[] uselessPos : useless2) {
      visitedMap2.markUseless(uselessPos[0], uselessPos[1]);
    }
    assertNotEquals("Visited maps shouln't be equal before merge", visitedMap1, visitedMap2);
    VisitedMap.mergeUseless(visitedMap1, visitedMap2);
    assertNotSame(
        "Visited maps containing only USELESS markings shouln't be the same object...",
        visitedMap1,
        visitedMap2);
    assertEquals("... but should be equal after merge", visitedMap1, visitedMap2);
  }

  @Test
  public void markLastMovesUselessTest() {
    int maxX = 6, maxY = 6;
    VisitedMap visitedMap1 = new VisitedMap(maxX, maxY);
    // open 0,2 and 2,4 and path through y=3
    for (int i = 0; i < maxX; i++) {
      if (i != 0) {
        visitedMap1.markWall(i, 2);
      }
      if (i != 2) {
        visitedMap1.markWall(i, 4);
      }
    }
    visitedMap1.markWall(maxX - 1, 3);

    // simulate walk through path y=3
    for (int i = 0; i < maxX - 1; i++) {
      visitedMap1.visit(i, 3);
      assertTrue(visitedMap1.isVisited(i, 3));
    }
    assertEquals(maxX - 1, visitedMap1.getLastVisited().size());

    visitedMap1.backtrackToUnknown();
    for (int i = 3; i < maxX - 1; i++) {
      assertTrue(
          "backtrack should mark visited positions as useless until next to unknown field",
          visitedMap1.isUseless(i, 3));
    }
    assertFalse(
        "backtrack should not mark 2,3 as useless (because 2,4 is unknown)",
        visitedMap1.isUseless(2, 3));
    assertEquals(3, visitedMap1.getLastVisited().size());

    visitedMap1.backtrackToUnknown();
    assertEquals(
        "backtrack should have no effect if map wasn't changed",
        3,
        visitedMap1.getLastVisited().size());
    assertFalse(
        "backtrack should not mark 2,3 as useless (because 2,4 is unknown)",
        visitedMap1.isUseless(2, 3));

    for (int i = maxX - 2; i > 2; i--) {
      visitedMap1.visit(i, 3);
    }
    assertEquals(
        "backtrack should have no effect because visited fields were already useless",
        3,
        visitedMap1.getLastVisited().size());
    visitedMap1.backtrackToUnknown();
    assertEquals(
        "backtrack should have no effect because visited fields were already useless",
        3,
        visitedMap1.getLastVisited().size());
    assertFalse(
        "backtrack should not mark 2,3 as useless (because 2,4 is unknown)",
        visitedMap1.isUseless(2, 3));
  }
}
