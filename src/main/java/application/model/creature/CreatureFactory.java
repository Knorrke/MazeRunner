package application.model.creature;

import java.util.ArrayList;
import java.util.List;

import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.creature.vision.Vision;
import application.model.maze.MazeModelInterface;

public class CreatureFactory {

	public static List<Creature> createAll(MazeModelInterface maze, CreatureGroup creatures) {
		ArrayList<Creature> list = new ArrayList<>();
		for (int i=0; i<creatures.getNumber(); i++) {
			list.add(create(maze, creatures.getType()));
		}
		return list;
	}
	
	public static Creature create(MazeModelInterface maze, CreatureType type, double x, double y) {
		double velocity=1;
		int lifes = 10;
		MovementInterface movementStrategy = new NoSightMovement();
		
		return new Creature(x, y, velocity, lifes, movementStrategy, new Vision(), type, maze);
	}

	public static Creature create(MazeModelInterface maze, CreatureType type) {
		return create(maze, type, 0, 0);
	}
}
