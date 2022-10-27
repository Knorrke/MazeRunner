package org.mazerunner.model.creature.movements;

import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.model.maze.MapNode;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.maze.MazeNode;

public class PerfectMovement implements MovementInterface {

  private MazeModelInterface maze;

  public PerfectMovement(MazeModelInterface maze2) {
    this.maze = maze2;
  }

  @Override
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY) {
    MapNode nextGoal = maze.getPerfectMoveMap().get(new MazeNode((int) currentX, (int) currentY));
    return new double[] {nextGoal.getX() + currentX % 1, nextGoal.getY() + currentY % 1};
  }
}
