package application.model.maze;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import application.controller.gameloop.ActorInterface;
import application.model.creature.Creature;
import application.model.player.PlayerUpdaterInterface;
import application.util.ObservableCreaturesListDeserializer;
import application.util.ObservableWallsListDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Maze implements MazeModelInterface {

  @JsonDeserialize(using = ObservableWallsListDeserializer.class)
  private ObservableList<Wall> walls = FXCollections.observableArrayList();

  // boolean array for fast lookup
  @JsonIgnore private boolean[][] hasWall;

  @JsonDeserialize(using = ObservableCreaturesListDeserializer.class)
  private ObservableList<Creature> creatures = FXCollections.observableArrayList();

  private final int maxWallX, maxWallY;

  @JsonIgnore private PlayerUpdaterInterface playerUpdater;

  public Maze() {
    this(20, 10);
  }

  public Maze(int maxX, int maxY) {
    maxWallX = maxX;
    maxWallY = maxY;
    hasWall = new boolean[maxWallX][maxWallY];
  }

  @Override
  public ObservableList<Wall> getWalls() {
    return walls;
  }

  public void setWalls(ObservableList<Wall> walls) {
    this.walls = walls;
    for (int i = 0; i < maxWallX; i++) {
      for (int j = 0; j < maxWallY; j++) {
        hasWall[i][j] = false;
      }
    }
    for (Wall wall : walls) {
      hasWall[wall.getX()][wall.getY()] = true;
    }
  }

  @Override
  public void addWall(Wall wall) {
    int x = wall.getX();
    int y = wall.getY();
    if (checkBounds(x, y) && !hasWallOn(x, y)) {
      this.walls.add(wall);
      hasWall[x][y] = true;
    }
  }

  @Override
  public Wall buildWall(int x, int y) {
    if (checkBounds(x, y) && !hasWallOn(x, y)) {
      Wall wall = new Wall(x, y);
      boolean successfull = playerUpdater != null ? playerUpdater.newWallBuilt(wall) : true;
      if (successfull) {
        this.addWall(wall);
        return wall;
      }
    }

    return null;
  }

  @Override
  public void removeWall(Wall wall) {
    this.walls.remove(wall);
    hasWall[wall.getX()][wall.getY()] = false;
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
    return hasWall[x][y];
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
    for (Iterator<Creature> iter = creatures.iterator(); iter.hasNext(); ) {
      Creature creature = iter.next();
      creature.act(dt);
      if (creature.getX() >= maxWallX - 1) {
        iter.remove();
        if (playerUpdater != null) {
          playerUpdater.leavingCreature(creature);
        }
      }
    }
    for (ActorInterface wall : walls) {
      wall.act(dt);
    }
  }

  @JsonIgnore
  @Override
  public MazeUpdaterInterface createUpdater() {
    return new MazeUpdater(this);
  }

  @Override
  public void setPlayerUpdater(PlayerUpdaterInterface playerUpdater) {
    this.playerUpdater = playerUpdater;
  }

  @Override
  public void sell(Wall wall) {
    removeWall(wall);
    playerUpdater.soldWall(wall);
  }
}
