package application.model.creature.movements;

import application.model.creature.VisitedMap;
import application.model.creature.vision.Vision;
import application.model.maze.MazeModelInterface;

public interface MovementInterface {
	public double[] getMoveDirection(MazeModelInterface maze, Vision vision, VisitedMap visited, double currentX, double currentY);
}
