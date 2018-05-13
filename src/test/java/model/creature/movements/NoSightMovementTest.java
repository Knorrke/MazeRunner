package model.creature.movements;

import static org.junit.Assert.*;

import application.model.creature.VisitedMap;
import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.creature.vision.Vision;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class NoSightMovementTest {
	int maxX = 5, maxY = 5;
	double x,y;
	MazeModelInterface maze;
	MovementInterface movement;
	Vision vision;
	VisitedMap visited;

	@Before
	public void setup () {
		maze = new Maze(maxX, maxY);
		movement = new NoSightMovement();
		vision = new Vision();
		visited = new VisitedMap(maxX, maxY);
		
		x = 2; y=3;
		maze.buildWall((int) x+1, (int) y); //in front (in x)
		maze.buildWall((int) x, (int) y+1); //above
		maze.buildWall((int) x, (int) y-1); //below
	}
	
	@Test
	public void moveForwardIfEmpty() {	
		double[] nextPos = movement.getMoveDirection(maze, vision, visited,0,y);
		assertArrayEquals("Creature should move forward (in x direction)", new double[]{1,0}, nextPos, 0.1);
	}
	
	@Test
	public void moveOutOfBlindAllay() {
		double[] nextPos = movement.getMoveDirection(maze, vision, visited, x, y);
		assertArrayEquals("Creature should move out of blind alley", new double[]{-1, 0}, nextPos, 0.1);
	}
	
	@Test
	public void avoidUselessSquares() {
		visited.markUseless((int) x,(int) y);
		double[] nextPos = movement.getMoveDirection(maze, vision, visited, x-1, y);
		assertFalse("Should not move to useless square", Arrays.equals(new double[]{1, 0}, nextPos));
	}

}
