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
  public void mergeOnlyVisitedTest() {
    int[][] visitedPos1 = {{0, 4}, {0, 3}, {0, 2}};
    int[][] visitedPos2 = {{4, 4}, {4, 3}, {4, 2}};
    for (int[] visitedPos : visitedPos1) {
      visitedMap.markVisited(visitedPos[0], visitedPos[1]);
    }
    VisitedMap visitedMap2 = new VisitedMap(maxX, maxY);
    for (int[] visitedPos : visitedPos2) {
      visitedMap2.markVisited(visitedPos[0], visitedPos[1]);
    }
    assertNotEquals("Visited maps shouln't be equal before merge", visitedMap, visitedMap2);
    VisitedMap.merge(visitedMap, visitedMap2);
    assertNotSame(
        "Visited maps containing only VISITED markings shouln't be the same object...",
        visitedMap,
        visitedMap2);
    assertEquals("... but should be equal after merge", visitedMap, visitedMap2);
  }

  @Test
  public void mergeWithWallsTest() {
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
}
