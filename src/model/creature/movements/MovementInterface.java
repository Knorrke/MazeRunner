package model.creature.movements;

import model.creature.VisitedMap;
import model.creature.vision.Vision;
import model.maze.MazeModelInterface;

public interface MovementInterface {
	public double[] getMoveDirection(MazeModelInterface maze, Vision vision, VisitedMap visited, double currentX, double currentY);
}
