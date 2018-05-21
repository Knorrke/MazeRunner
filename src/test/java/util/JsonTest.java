package util;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import java.util.Random;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import application.model.Game;
import application.model.GameModelInterface;
import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import application.model.creature.movements.NoSightMovement;
import application.model.level.Level;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.NoTower;
import application.model.player.Player;
import application.model.player.PlayerModelInterface;
import application.util.Serializer;

public class JsonTest {

  private static Logger LOG = Logger.getLogger(JsonTest.class.getName());
  Gson gson;

  @Before
  public void setUp() {
    gson = Serializer.create();
  }

  @Test
  public void wallToJson() {
    int x = 2, y = 3;
    Wall wall = new Wall(x, y);
    JsonObject obj = gson.toJsonTree(wall).getAsJsonObject();
    LOG.fine(obj.toString());
    
    assertThat(gson.fromJson(obj, Wall.class), is(instanceOf(Wall.class)));
  }

  @Test
  public void towerToJson() {
    AbstractTower tower = new NoTower();
    String json = gson.toJson(tower);
    LOG.fine(json);

    assertThat(gson.fromJson(json, NoTower.class), is(instanceOf(NoTower.class)));
  }

  @Test
  public void creatureToJson() {
    int x = 2, y = 3;
    Maze maze = new Maze();
    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, x, y);
    JsonObject obj = gson.toJsonTree(creature).getAsJsonObject();
    LOG.fine(obj.toString());

    assertThat(gson.fromJson(obj, Creature.class), is(instanceOf(Creature.class)));
  }

  @Test
  public void movementStrategyToJson() {
    String json = gson.toJson(new NoSightMovement());
    LOG.fine(json);

    assertThat(gson.fromJson(json, NoSightMovement.class), is(instanceOf(NoSightMovement.class)));
  }
  
  @Test
  public void mazeToJson() {
    String json = gson.toJson(new Maze());
    LOG.fine(json);
    assertThat(gson.fromJson(json, Maze.class), is(instanceOf(Maze.class)));
  }
  
  @Test
  public void mazeWithObjToJson() {
    Maze maze = new Maze();
    maze.buildWall(1, 1);
    maze.buildWall(1, 2);
    maze.buildWall(1, 3);
    String json = gson.toJson(maze);
    Maze maze2 = gson.fromJson(json, Maze.class);
    assertEquals(maze.getCreatures().size(), maze2.getCreatures().size());
    assertEquals(maze.getWalls().size(), maze2.getWalls().size());
  }

  @Test
  public void playerToJson() {
    int money = 50, lifes = 20;
    PlayerModelInterface player = new Player(money, lifes);
    JsonObject obj = gson.toJsonTree(player).getAsJsonObject();
    LOG.fine(obj.toString());

    assertThat(gson.fromJson(obj, Player.class), is(instanceOf(Player.class)));
  }

  @Test
  public void levelToJson() {}

  @Test
  public void gameToJson() {
    GameModelInterface[] games = new GameModelInterface[1];
    games[0] = new Game();
    JsonArray arr = gson.toJsonTree(games).getAsJsonArray();
    JsonObject obj = arr.get(0).getAsJsonObject();
    LOG.fine(obj.toString());
    assertThat(gson.fromJson(obj, Game.class), is(instanceOf(Game.class)));
  }

  @Test
  public void gameWithCreaturesAndWallsToJson() {
    GameModelInterface[] games = new GameModelInterface[1];
    games[0] = new Game();
    GameModelInterface game = games[0];
//    GameModelInterface game = new Game();
    MazeModelInterface maze = game.getMaze();
    maze.buildWall(1, 1);
    maze.buildWall(1, 2);
    maze.buildWall(2, 3);
    maze.addAllCreatures(
        CreatureFactory.createAll(
            maze,
            new CreatureGroup(CreatureType.NORMAL, 20),
            () -> (double) new Random().nextInt(),
            () -> (double) new Random().nextInt()));
    
    JsonArray arr = gson.toJsonTree(games).getAsJsonArray();
    LOG.fine(arr.toString());
    System.out.println(arr.toString()); 
    GameModelInterface[] games2 = gson.fromJson(arr, Game[].class);
//    JsonObject obj = arr.get(0).getAsJsonObject();
    GameModelInterface game2 = games2[0];
//    GameModelInterface game2 = gson.fromJson(obj, Game.class);
    System.out.println(gson.toJson(new GameModelInterface[] {game2})); 
    assertEquals(game.getMaze().getWalls().size(), game2.getMaze().getWalls().size());
    assertEquals(game.getMaze().getCreatures().size(), game2.getMaze().getCreatures().size());
  }
}
