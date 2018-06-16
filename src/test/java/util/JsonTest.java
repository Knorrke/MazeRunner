package util;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.model.Game;
import application.model.GameModelInterface;
import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import application.model.creature.movements.NoSightMovement;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.NoTower;
import application.model.maze.tower.TowerType;
import application.model.player.Player;
import application.model.player.PlayerModelInterface;
import application.util.Serializer;

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

    AbstractTower tower2 = serializer.readValue(json, NoTower.class);
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
  public void deserialize() throws IOException {
    String json =
        "{\"Game\":{\"state\":\"BUILDING\",\"player\":{\"Player\":{\"money\":50,\"lifes\":20}},\"level\":{\"Level\":{\"creatureTimeline\":[{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20},{\"type\":\"NORMAL\",\"number\":20}],\"waveNumber\":0}},\"maze\":{\"Maze\":{\"walls\":[{\"x\":2,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":3,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":4,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":5,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":6,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":8,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":9,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":10,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":11,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":13,\"y\":0,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":13,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":13,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":12,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":11,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":14,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":15,\"y\":2,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":15,\"y\":1,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":15,\"y\":0,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":9,\"y\":2,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":9,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":9,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":11,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":10,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":12,\"y\":4,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":8,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":7,\"y\":4,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":7,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":6,\"y\":2,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":2,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":3,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":4,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":7,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":5,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":6,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":8,\"y\":9,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":2,\"y\":7,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":2,\"y\":6,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":2,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":1,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":1,\"y\":4,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":1,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":3,\"y\":2,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":3,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":4,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":5,\"y\":4,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":5,\"y\":3,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":4,\"y\":7,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":6,\"y\":6,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":6,\"y\":7,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":8,\"y\":7,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":9,\"y\":7,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":10,\"y\":7,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":14,\"y\":6,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":14,\"y\":5,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":12,\"y\":6,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":15,\"y\":7,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":15,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":15,\"y\":9,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":13,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":12,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}},{\"x\":11,\"y\":8,\"tower\":{\"type\":\"NO\",\"bullets\":[]}}],\"creatures\":[],\"maxWallX\":20,\"maxWallY\":10}}}}";
    Game game = serializer.readValue(json, Game.class);
    String serialized = serializer.writeValueAsString(game);
    LOG.fine(serialized);
    assertEquals(json, serialized);
  }
}
