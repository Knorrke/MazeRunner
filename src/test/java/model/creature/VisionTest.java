package model.creature;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.util.Util;

public class VisionTest {
  private int maxX = 10;
  private int maxY = 10;
  private MazeModelInterface maze;
  private VisitedMap map;

  @Before
  public void setup() {
    maze = new Maze(maxX, maxY);
    map = new VisitedMap(maxX, maxY);
    for (int x = 4; x < maxX; x++) {
      maze.buildWall(x, 1);
    }
    maze.buildWall(9, 0);
  }

  @Test
  public void updateVisitedMapTest() {
    Vision vision = new Vision(Integer.MAX_VALUE, maze);
    int viewerX = 0;
    int viewerY = maxY - 1;
    vision.updateVisitedMap(map, viewerX, viewerY);
    for (int y = 0; y < maxY; y++) {
      for (int x = 0; x < maxX; x++) {
        if (y == 0 && x > 4) {
          assertTrue(
              "Shouldn't be able to see behind wall (at " + x + "," + y + ")", map.isUnknown(x, y));
        } else if (y == 1 && x >= 4) {
          assertTrue("Should be able to see the wall (at " + x + "," + y + ")", map.isWall(x, y));
        } else {
          assertTrue("Should see everything else (at " + x + "," + y + ")", map.isVisited(x, y));
        }
      }
    }
  }

  @Test
  public void shorterSightTest() {
    for (int radius = 1; radius < 10; radius++) {
      Vision vision = new Vision(radius, maze);
      double viewerX = 6.5;
      double viewerY = 3.5;
      vision.updateVisitedMap(map, viewerX, viewerY);
      for (int y = 0; y < maxY; y++) {
        for (int x = 0; x < maxX; x++) {
          if (Util.distance(viewerX, viewerY, x, y) > radius
              && Util.distance(viewerX, viewerY, x + 1, y) > radius
              && Util.distance(viewerX, viewerY, x, y + 1) > radius
              && Util.distance(viewerX, viewerY, x + 1, y + 1) > radius) {
            assertTrue(
                String.format(
                    "Shouldn't be able to see further than radius. (at %d,%d with r=%d)",
                    x, y, radius),
                map.isUnknown(x, y));
          } else {
            if (y == 0 && (x > 2 || x == 2 && radius <= 5)) {
              assertTrue(
                  String.format(
                      "Shouldn't be able to see behind wall  (at %d,%d with r=%d)", x, y, radius),
                  map.isUnknown(x, y));
            } else if (y == 1 && x >= 4) {
              assertTrue(
                  String.format(
                      "Should be able to see the wall  (at %d,%d with r=%d)", x, y, radius),
                  map.isWall(x, y));
            } else {
              assertTrue(
                  String.format("Should see everything else  (at %d,%d with r=%d)", x, y, radius),
                  map.isVisited(x, y));
            }
          }
        }
      }
    }
  }
}
