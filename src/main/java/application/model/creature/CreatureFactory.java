package application.model.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import application.model.creature.movements.MovementInterface;
import application.model.creature.movements.NoSightMovement;
import application.model.creature.vision.Vision;
import application.model.maze.MazeModelInterface;

public class CreatureFactory {

  public static Creature create(MazeModelInterface maze, CreatureType type, double x, double y) {
    double velocity = 1;
    int lifes = 10;
    MovementInterface movementStrategy = new NoSightMovement();

    return new Creature(x, y, velocity, lifes, movementStrategy, new Vision(), type, maze);
  }

  public static Creature create(MazeModelInterface maze, CreatureType type) {
    return create(maze, type, 0, 0);
  }

  public static List<Creature> createAll(
      MazeModelInterface maze, CreatureGroup creatures, Random randomX, Random randomY) {
    return createAll(
        maze,
        creatures,
        () -> randomX.nextDouble() * maze.getMaxWallX(),
        () -> randomY.nextDouble() * maze.getMaxWallY());
  }

  public static List<Creature> createAll(
      MazeModelInterface maze, CreatureGroup creatures, Random randomX, double fixedY) {
    return createAll(
        maze, creatures, () -> randomX.nextDouble() * maze.getMaxWallX(), () -> fixedY);
  }

  public static List<Creature> createAll(
      MazeModelInterface maze, CreatureGroup creatures, double fixedX, Random randomY) {
    return createAll(
        maze, creatures, () -> fixedX, () -> randomY.nextDouble() * maze.getMaxWallY());
  }

  public static List<Creature> createAll(
      MazeModelInterface maze, CreatureGroup creatures, double fixedX, double fixedY) {
    return createAll(maze, creatures, () -> fixedX, () -> fixedY);
  }

  public static List<Creature> createAll(
      MazeModelInterface maze,
      CreatureGroup creatures,
      Callable<Double> xGenerator,
      Callable<Double> yGenerator) {
    ArrayList<Creature> list = new ArrayList<>();
    for (int i = 0; i < creatures.getNumber(); i++) {
      double x, y;
      try {
        x = xGenerator.call() % maze.getMaxWallX();
        y = yGenerator.call() % maze.getMaxWallY();

        if (x < 0) x += maze.getMaxWallX();
        if (y < 0) y += maze.getMaxWallY();
      } catch (Exception e) {
        x = 0;
        y = 0;
        e.printStackTrace();
      }
      list.add(create(maze, creatures.getType(), x, y));
    }
    return list;
  }
}
