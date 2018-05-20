package util;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.creature.movements.NoSightMovement;
import application.model.maze.Maze;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.NoTower;
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
    assertTrue(obj.has("x"));
    assertEquals(obj.get("x").getAsInt(), x);

    assertTrue(obj.has("y"));
    assertEquals(obj.get("y").getAsInt(), y);

    assertTrue(obj.has("tower"));
    assertTrue(obj.get("tower").isJsonObject());
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

    assertTrue(obj.has("x"));
    assertEquals(obj.get("x").getAsDouble(), x, 0.01);

    assertTrue(obj.has("y"));
    assertEquals(obj.get("y").getAsDouble(), y, 0.01);

    assertTrue(obj.has("velocity"));
    assertTrue(obj.get("velocity").isJsonPrimitive());

    assertTrue(obj.has("lifes"));
    assertTrue(obj.get("lifes").isJsonPrimitive());

    assertTrue(obj.has("type"));
    assertTrue(obj.get("type").isJsonPrimitive());

    assertFalse(obj.has("maze"));

    assertTrue(obj.has("vision"));
    assertTrue(obj.get("vision").isJsonObject());

    assertTrue(obj.has("map"));
    assertTrue(obj.get("map").isJsonObject());

    assertTrue(obj.has("movementStrategy"));
    assertTrue(obj.get("movementStrategy").isJsonObject());
    assertThat(gson.fromJson(obj, Creature.class), is(instanceOf(Creature.class)));
  }

  @Test
  public void movementStrategyToJson() {
    String json = gson.toJson(new NoSightMovement());
    LOG.fine(json);

    assertThat(gson.fromJson(json, NoSightMovement.class), is(instanceOf(NoSightMovement.class)));
  }
}
