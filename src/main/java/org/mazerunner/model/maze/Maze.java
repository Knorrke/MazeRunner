package org.mazerunner.model.maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

  @JsonIgnore private Map<Node, Node> perfectMoveMap;

  @JsonIgnore private ObjectProperty<GameError> error;

  public Maze() {
    this(20, 10);
  }

  public Maze(int maxX, int maxY) {
    maxWallX = maxX;
    maxWallY = maxY;
    hasWall = new boolean[maxWallX][maxWallY];
    error = new SimpleObjectProperty<>();
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
      perfectMoveMap = calculatePerfectMoveMap();
    }
  }

  @Override
  public Wall buildWall(int x, int y) {
    if (checkBounds(x, y) && !hasWallOn(x, y)) {
      Wall wall = new Wall(x, y, AbstractTower.create(TowerType.NO));
      this.addWall(wall);
      for (int line = 0; line < maxWallY; line++) {
        if (!perfectMoveMap.containsKey(new Node(0, line))) {
          this.removeWall(wall);
          setError(this::buildingWallNotAllowedError);
          return null;
        }
      }
      for (Creature creature : getCreatures()) {
        if (!perfectMoveMap.containsKey(new Node(creature.getX(), creature.getY()))) {
          this.removeWall(wall);
          setError(this::buildingWallNotAllowedError);
          return null;
        }
      }
      if (payIfEnoughMoney(wall.getCosts())) {
        for (Creature creature : creatures) {
          VisitedMap map = creature.getVisitedMap();
          if (map.isVisited(x, y)) {
            map.markWall(x, y);
          }
        }
        this.addWall(wall);
        return wall;
      } else {
        this.removeWall(wall);
        setError(this::notEnoughMoneyError);
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

  public Map<Node, Node> getPerfectMoveMap() {
    return perfectMoveMap;
  }

  private Map<Node, Node> calculatePerfectMoveMap() {
    Map<Node, Node> previous = new HashMap<>();
    Map<Node, Integer> dist = new HashMap<>();
    List<Node> closed = new ArrayList<>();
    List<Node> next = new ArrayList<>();
    Node fictiveGoal = new Node(-1, -1);

    fictiveGoal.neighbors = new ArrayList<>();
    for (int y = 0; y < maxWallY; y++) {
      fictiveGoal.neighbors.add(new Node(maxWallX - 1, 0));
    }
    dist.put(fictiveGoal, 0);
    next.add(fictiveGoal);
    while (!next.isEmpty()) {
      Collections.sort(
          next,
          (n1, n2) -> {
            int dist1 = dist.containsKey(n1) ? dist.get(n1) : Integer.MAX_VALUE;
            int dist2 = dist.containsKey(n2) ? dist.get(n2) : Integer.MAX_VALUE;
            return dist1 - dist2;
          });
      Node nextEl = next.remove(0);
      closed.add(nextEl);
      if (nextEl.neighbors == null) {
        nextEl.neighbors = new ArrayList<>();
        if (checkBounds(nextEl.x - 1, nextEl.y))
          nextEl.neighbors.add(new Node(nextEl.x - 1, nextEl.y));
        if (checkBounds(nextEl.x + 1, nextEl.y))
          nextEl.neighbors.add(new Node(nextEl.x + 1, nextEl.y));
        if (checkBounds(nextEl.x, nextEl.y - 1))
          nextEl.neighbors.add(new Node(nextEl.x, nextEl.y - 1));
        if (checkBounds(nextEl.x, nextEl.y + 1))
          nextEl.neighbors.add(new Node(nextEl.x, nextEl.y + 1));
      }
      for (Node neighbor : nextEl.neighbors) {
        if (closed.contains(neighbor)) continue;
        if (hasWallOn(neighbor.x, neighbor.y)) {
          closed.add(neighbor);
          continue;
        } else {
          if (!next.contains(neighbor)) next.add(neighbor);

          if (dist.containsKey(nextEl)
              && (!dist.containsKey(neighbor) || dist.get(nextEl) + 1 < dist.get(neighbor))) {
            dist.put(neighbor, dist.get(nextEl) + 1);
            previous.put(neighbor, nextEl);
          }
        }
      }
    }
    return previous;
  }

  public ObjectProperty<GameError> errorProperty() {
    return error;
  }

  public void setError(GameError error) {
    this.error.set(error);
  }

  public GameError getError() {
    return error.get();
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

  private class Node {
    public int x, y;
    public List<Node> neighbors;

    public Node(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Node(double x, double y) {
      this((int) x, (int) y);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Node)) {
        return false;
      } else {
        Node other = (Node) obj;
        return x == other.x && y == other.y;
      }
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(new int[] {x, y});
    }
  }
}
