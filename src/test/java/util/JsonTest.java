package util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.Game;
import org.mazerunner.model.GameModelInterface;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.creature.movements.NoSightMovement;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.maze.tower.AbstractTower;
import org.mazerunner.model.maze.tower.NoTower;
import org.mazerunner.model.maze.tower.TowerType;
import org.mazerunner.model.player.Player;
import org.mazerunner.model.player.PlayerModelInterface;
import org.mazerunner.util.Serializer;
import org.mockito.Mockito;

public class JsonTest {

  private static Logger LOG = Logger.getLogger(JsonTest.class.getName());
  ObjectMapper serializer;

  @Before
  public void setUp() {
    serializer = Serializer.create();
  }

  @Test
  public void wallToJson() throws IOException {
    int x = 2, y = 3;
    Wall wall = new Wall(x, y);
    wall.setTower(AbstractTower.create(wall, TowerType.NO));
    String json = serializer.writeValueAsString(wall);
    LOG.fine(json);
    Wall wall2 = serializer.readValue(json, Wall.class);
    assertThat(wall2, is(instanceOf(Wall.class)));
    assertEquals(json, serializer.writeValueAsString(wall2));
  }

  @Test
  public void towerToJson() throws IOException {
    AbstractTower tower = AbstractTower.create(Mockito.mock(Wall.class), TowerType.NO);
    String json = serializer.writeValueAsString(tower);
    LOG.fine(json);

    AbstractTower tower2 = serializer.readValue(json, AbstractTower.class);
    assertThat(tower2, is(instanceOf(NoTower.class)));
    assertEquals(json, serializer.writeValueAsString(tower2));
  }

  @Test
  public void creatureToJson() throws IOException {
    int x = 2, y = 3;
    Maze maze = new Maze();
    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, x, y);
    String json = serializer.writeValueAsString(creature);
    LOG.fine(json);

    Creature creature2 = serializer.readValue(json, Creature.class);
    assertThat(creature2, is(instanceOf(Creature.class)));
    assertEquals(json, serializer.writeValueAsString(creature2));
  }

  @Test
  public void movementStrategyToJson() throws IOException {
    String json = serializer.writeValueAsString(new NoSightMovement());
    LOG.fine(json);

    NoSightMovement movement2 = serializer.readValue(json, NoSightMovement.class);
    assertThat(movement2, is(instanceOf(NoSightMovement.class)));
    assertEquals(json, serializer.writeValueAsString(movement2));
  }

  @Test
  public void mazeToJson() throws IOException {
    String json = serializer.writeValueAsString(new Maze());
    LOG.fine(json);
    Maze maze2 = serializer.readValue(json, Maze.class);
    assertThat(maze2, is(instanceOf(Maze.class)));
    assertEquals(json, serializer.writeValueAsString(maze2));
  }

  @Test
  public void mazeWithObjToJson() throws IOException {
    Maze maze = new Maze();
    maze.buildWall(1, 1);
    maze.buildWall(1, 2);
    maze.buildWall(1, 3);
    String json = serializer.writeValueAsString(maze);
    LOG.fine(json);
    Maze maze2 = serializer.readValue(json, Maze.class);
    assertEquals(maze.getCreatures().size(), maze2.getCreatures().size());
    assertEquals(maze.getWalls().size(), maze2.getWalls().size());

    assertEquals(json, serializer.writeValueAsString(maze2));
  }

  @Test
  public void playerToJson() throws IOException {
    int money = 50, lifes = 20;
    PlayerModelInterface player = new Player(money, lifes);
    String json = serializer.writeValueAsString(player);
    LOG.fine(json);
    Player player2 = serializer.readValue(json, Player.class);
    assertThat(player2, is(instanceOf(Player.class)));
    assertEquals(json, serializer.writeValueAsString(player2));
  }

  @Test
  public void levelToJson() {}

  @Test
  public void gameToJson() throws IOException {
    GameModelInterface game = new Game();
    String json = serializer.writeValueAsString(game);
    LOG.fine(json);
    Game game2 = serializer.readValue(json, Game.class);
    assertThat(game2, is(instanceOf(Game.class)));
    assertEquals(json, serializer.writeValueAsString(game2));
  }

  @Test
  public void gameWithCreaturesAndWallsToJson() throws IOException {
    GameModelInterface game = new Game();
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

    String json = serializer.writeValueAsString(game);
    LOG.fine(json);
    GameModelInterface game2 = serializer.readValue(json, Game.class);
    assertEquals(game.getMaze().getWalls().size(), game2.getMaze().getWalls().size());
    assertEquals(game.getMaze().getCreatures().size(), game2.getMaze().getCreatures().size());
    assertEquals(json, serializer.writeValueAsString(game2));
  }

  @Test
  public void upgradedTowerToJson() throws IOException {
    AbstractTower upgradedTower = AbstractTower.create(TowerType.NORMAL).upgrade();
    String json = serializer.writeValueAsString(upgradedTower);
    LOG.fine(json);
    AbstractTower tower2 = serializer.readValue(json, AbstractTower.class);
    assertEquals(1, tower2.getLevel());
    assertEquals(json, serializer.writeValueAsString(tower2));
  }
}
