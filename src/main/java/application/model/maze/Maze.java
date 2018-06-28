package application.model.maze;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import application.controller.gameloop.ActorInterface;
import application.model.creature.Creature;
import application.model.creature.VisitedMap;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.TowerType;
import application.model.maze.tower.TowerUpgrade;
import application.model.player.PlayerModelInterface;
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

  @JsonIgnore private PlayerModelInterface player;

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
      wall.setMaze(this);
    }
  }

  private void addWall(Wall wall) {
    int x = wall.getX();
    int y = wall.getY();
    if (checkBounds(x, y) && !hasWallOn(x, y)) {
      this.walls.add(wall);
      wall.setMaze(this);
      hasWall[x][y] = true;
    }
  }

  @Override
  public Wall buildWall(int x, int y) {
    if (checkBounds(x, y) && !hasWallOn(x, y)) {
      Wall wall = new Wall(x, y, AbstractTower.create(TowerType.NO));
      if (payIfEnoughMoney(wall.getCosts())) {
        for (Creature creature : creatures) {
          VisitedMap map = creature.getVisitedMap();
          if (map.isVisited(x, y)) {
            map.markWall(x, y);
          }
        }
        this.addWall(wall);
        return wall;
      }
    }

    return null;
  }

  private void removeWall(Wall wall) {
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

  private boolean removeCreature(Creature creature) {
    return this.creatures.remove(creature);
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
  public Wall getWallOn(int x, int y) {
    if (hasWallOn(x, y)) {
      for (Wall wall : walls) {
        if (wall.getX() == x && wall.getY() == y) {
          return wall;
        }
      }
    }
    return null;
  }

  @Override
  public boolean hasCreatureNear(double x, double y) {
    return getCreatureNear(x, y) != null;
  }

  @Override
  public Creature getCreatureNear(double x, double y) {
    for (Creature creature : creatures) {
      if (Point.distance(creature.getX(), creature.getY(), x, y) < 0.3) {
        return creature;
      }
    }
    return null;
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
        if (player != null) {
          player.looseLife();
        }
      }
    }
    for (ActorInterface wall : walls) {
      wall.act(dt);
    }
  }

  @Override
  public void setPlayerModel(PlayerModelInterface player) {
    this.player = player;
  }

  @Override
  public void sell(Wall wall) {
    for (Creature creature : creatures) {
      VisitedMap map = creature.getVisitedMap();
      if (map.isWall(wall.getX(), wall.getY())) {
        map.markUnknown(wall.getX(), wall.getY());
      }
    }

    removeWall(wall);
    if (player != null) {
      player.earnMoney(wall.getCosts());
    }
  }

  @Override
  public void creatureDied(Creature creature) {
    if (removeCreature(creature) && player != null) {
      player.earnMoney(creature.getValue());
    }
  }

  @Override
  public void buildTower(Wall wall, TowerType type) {
    AbstractTower newTower = AbstractTower.create(type);
    if (wall.canBuildTower(type) && payIfEnoughMoney(newTower.getCosts())) {
      wall.buildTower(type);
    }
  }

  @Override
  public void upgradeTower(Wall wall) {
    TowerUpgrade nextUpgrade = wall.getTower().getNextUpgrade();
    if (nextUpgrade != null && payIfEnoughMoney(nextUpgrade.getCosts())) {
      wall.upgradeTower();
    }
  }

  private boolean payIfEnoughMoney(int costs) {
    return player != null ? player.spendMoney(costs) : true;
  }
}
