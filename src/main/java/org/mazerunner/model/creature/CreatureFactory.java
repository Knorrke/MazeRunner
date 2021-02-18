package org.mazerunner.model.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import org.mazerunner.model.creature.movements.MovementInterface;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.MazeModelInterface;

public class CreatureFactory {
  private CreatureFactory() {}

  public static Creature create(MazeModelInterface maze, CreatureType type, double x, double y) {
    return create(maze, type, x, y, 1);
  }

  private static Creature create(
      MazeModelInterface maze, CreatureType type, double x, double y, double timeFactor) {
    double velocity = type.getDefaultVelocity();
    int lifes = (int) (type.getDefaultLifes() * timeFactor);
    int value = (int) (type.getDefaultValue() * Math.sqrt(timeFactor * 2));
    MovementInterface movementStrategy = type.getMovementStrategy();
    Vision vision = new Vision(type.getDefaultVisionRadius(), maze);
    return new Creature(x, y, velocity, lifes, value, movementStrategy, vision, type, maze);
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
      Supplier<Double> xGenerator,
      Supplier<Double> yGenerator) {
    ArrayList<Creature> list = new ArrayList<>();
    for (int i = 0; i < creatures.getNumber(); i++) {
      double x = xGenerator.get() % maze.getMaxWallX();
      double y = yGenerator.get() % maze.getMaxWallY();

      if (x < 0) x += maze.getMaxWallX();
      if (y < 0) y += maze.getMaxWallY();
      list.add(create(maze, creatures.getType(), x, y, creatures.getToughnessFactor()));
    }
    return list;
  }

  public static Creature createExampleOfGroup(CreatureGroup group) {
    return create(new Maze(2, 1), group.getType(), 0, 0, group.getToughnessFactor());
  }
}
