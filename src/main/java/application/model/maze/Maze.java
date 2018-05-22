package application.model.maze;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import application.model.creature.Creature;
import application.model.gameloop.ActorInterface;
import application.util.ObservableListDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Maze implements MazeModelInterface {
  @JsonDeserialize(using = ObservableListDeserializer.forWalls.class)
  private ObservableList<Wall> walls = FXCollections.observableArrayList();
  @JsonDeserialize(using = ObservableListDeserializer.forCreatures.class)
  private ObservableList<Creature> creatures = FXCollections.observableArrayList();
  private final int maxWallX, maxWallY;

  public Maze() {
    this(20, 10);
  }

  public Maze(int maxX, int maxY) {
    maxWallX = maxX;
    maxWallY = maxY;
  }

  @Override
  public ObservableList<Wall> getWalls() {
    return walls;
  }

  @Override
  public void addWall(Wall wall) {
    int x = wall.getX();
    int y = wall.getY();
    if (checkBounds(x, y) && !hasWallOn(x, y)) {
      this.walls.add(wall);
    }
  }

  @Override
  public void buildWall(int x, int y) {
    this.addWall(new Wall(x, y));
  }

  @Override
  public void removeWall(Wall wall) {
    this.walls.remove(wall);
  }

  @Override
  public ObservableList<Creature> getCreatures() {
    return creatures;
  }

  @Override
  public void addCreature(Creature creature) {
    this.creatures.add(creature);
  }

  @Override
  public void removeCreature(Creature creature) {
    this.creatures.remove(creature);
  }

  @Override
  public void addAllCreatures(List<Creature> creatures) {
    this.creatures.addAll(creatures);
  }

  @Override
  public boolean hasWallOn(int x, int y) {
    return !getWalls().filtered(wall -> wall.getX() == x && wall.getY() == y).isEmpty();
  }

  @Override
  public int getMaxWallX() {
    return maxWallX;
  }

  @Override
  public int getMaxWallY() {
    return maxWallY;
  }

  @Override
  public void update(double dt) {
    for (ActorInterface creature : creatures) {
      creature.act(dt);
    }
    for (ActorInterface wall : walls) {
      wall.act(dt);
    }
  }
}
