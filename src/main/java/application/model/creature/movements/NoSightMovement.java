package application.model.creature.movements;

import java.util.List;
import java.util.stream.Collectors;

import application.model.creature.VisitedMap;
import application.model.creature.vision.Vision;
import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;

public class NoSightMovement implements MovementInterface {

  @Override
  public double[] getMoveDirection(
      MazeModelInterface maze,
      Vision vision,
      VisitedMap visited,
      double currentX,
      double currentY) {
    List<Wall> walls = maze.getWalls();
    List<Wall> surrounding =
        walls
            .stream()
            .filter(
                wall ->
                    Math.abs(wall.getX() - currentX) <= 1 && Math.abs(wall.getY() - currentY) <= 1)
            .collect(Collectors.toList());

    double[][] directions = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}};
    for (double[] direction : directions) {
      int x = (int) (currentX + direction[0]);
      int y = (int) (currentY + direction[1]);
      if (maze.checkBounds(x, y) && !isWallOn(x, y, surrounding) && visited.isUnknown(x, y)) {
        return direction;
      }
    }
    return null;
  }

  private boolean isWallOn(int x, int y, List<Wall> list) {
    return list.stream().anyMatch(wall -> wall.getX() == x && wall.getY() == y);
  }
}
