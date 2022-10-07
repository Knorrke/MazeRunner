package org.mazerunner.model.maze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.scenario.Settings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mazerunner.controller.gameloop.ActorInterface;
import org.mazerunner.model.GameError;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.model.maze.tower.TowerUpgrade;
import org.mazerunner.model.player.PlayerModelInterface;
import org.mazerunner.util.ObservableCreaturesListDeserializer;
import org.mazerunner.util.ObservableWallsListDeserializer;
import org.mazerunner.util.Util;

public class Maze implements MazeModelInterface {

  @JsonDeserialize(using = ObservableWallsListDeserializer.class)
  private ObservableList<Wall> walls = FXCollections.observableArrayList();

  // boolean array for fast lookup
  @JsonIgnore private boolean[][] hasWall;

  @JsonDeserialize(using = ObservableCreaturesListDeserializer.class)
  private ObservableList<Creature> creatures = FXCollections.observableArrayList();

  private final int maxWallX, maxWallY;

  @JsonIgnore private PlayerModelInterface player;

  @JsonIgnore private Map<MapNode, MapNode> perfectMoveMap;

  @JsonIgnore private ObjectProperty<GameError> error;

  private MapNode fictiveGoal;

  public Maze() {
    this(20, 10);
  }

  public Maze(int maxX, int maxY) {
    maxWallX = maxX;
    maxWallY = maxY;
    Settings.set("maxX", "" + maxX);
    Settings.set("maxY", "" + maxY);
    hasWall = new boolean[maxWallX][maxWallY];
    error = new SimpleObjectProperty<>();
    fictiveGoal =
        new MapNode(-1, -1) {
          @Override
          public List<MapNode> getNeighbors() {
            ArrayList<MapNode> neighbors = new ArrayList<>();
            for (int y = 0; y < maxWallY; y++) {
              neighbors.add(new MazeNode(maxWallX - 1, y));
            }
            return neighbors;
          }

          @Override
          public boolean isGoal() {
            return true;
          }
        };
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

    perfectMoveMap =
        GraphSolver.calculateShortestPaths(
            fictiveGoal, (nodeX, nodeY) -> !checkBounds(nodeX, nodeY) || hasWallOn(nodeX, nodeY));
  }

  @Override
  public Wall buildWall(int x, int y) {
    if (!checkBounds(x, y) || hasWallOn(x, y)) return null;

    Wall wall = new Wall(x, y, AbstractTower.create(TowerType.NO));

    if (!movementPossibleAfterBuild(wall)) {
      setError(this::buildingWallNotAllowedError);
      return null;
    }
    boolean success = payIfEnoughMoney(wall.getCosts());
    if (!success) {
      setError(this::notEnoughMoneyError);
      return null;
    }

    for (Creature creature : creatures) {
      VisitedMap map = creature.getVisitedMap();
      if (map.isVisited(x, y)) {
        map.markWall(x, y);
      }
    }
    this.addWall(wall);
    return wall;
  }

  private boolean movementPossibleAfterBuild(Wall wall) {
    var tmpMoveMap =
        GraphSolver.calculateShortestPaths(
            fictiveGoal,
            (nodeX, nodeY) ->
                (nodeX == wall.getX() && nodeY == wall.getY())
                    || !checkBounds(nodeX, nodeY)
                    || hasWallOn(nodeX, nodeY));
    for (int line = 0; line < maxWallY; line++) {
      if (!tmpMoveMap.containsKey(new MazeNode(0, line))) {
        return false;
      }
    }
    for (Creature creature : getCreatures()) {
      if (!tmpMoveMap.containsKey(new MazeNode((int) creature.getX(), (int) creature.getY()))) {
        return false;
      }
    }
    return true;
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
      if (Util.distance(creature.getX(), creature.getY(), x, y) < 0.3) {
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
        setError(this::cantSellError);
        return;
      }
    }

    removeWall(wall);
    if (player != null) {
      player.earnMoney(wall.getCosts());
    }
  }

  @Override
  public void sellTower(Wall wall) {
    if (player != null) {
      player.earnMoney(wall.getTower().getCosts());
    }
    wall.setTower(AbstractTower.create(TowerType.NO));
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

    if (!wall.hasTower()) {
      if (payIfEnoughMoney(newTower.getCosts())) {
        wall.buildTower(type);
      } else {
        setError(this::notEnoughMoneyError);
      }
    }
  }

  @Override
  public void upgradeTower(Wall wall) {
    TowerUpgrade nextUpgrade = wall.getTower().getNextUpgrade();
    if (nextUpgrade != null) {
      if (payIfEnoughMoney(nextUpgrade.getCosts())) {
        wall.upgradeTower();
      } else {
        setError(this::notEnoughMoneyError);
      }
    }
  }

  private boolean payIfEnoughMoney(int costs) {
    return player != null ? player.spendMoney(costs) : true;
  }

  public ObjectProperty<GameError> errorProperty() {
    return error;
  }

  public void setError(GameError error) {
    this.error.set(error);
  }

  public String buildingWallNotAllowedError() {
    return "Building there is not allowed. Don't trap the creatures!";
  }

  public String notEnoughMoneyError() {
    return "Not enough money";
  }

  public String cantSellError() {
    return "This wall can't be sold, because creatures already have seen it. Kill them first.";
  }
}
