package model.creature;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;

public class CreatureFactoryTest {

  private final CreatureType type = CreatureType.NORMAL;
  private final int number = 20;
  private MazeModelInterface maze;
  private CreatureGroup normalCreatureGroup;

  @Before
  public void setUp() {
    normalCreatureGroup = new CreatureGroup(type, number);
    maze = new Maze();
  }
  
  @Test
  public void createCreatureGroupTest() {
    List<Creature> creatures =
        CreatureFactory.createAll(maze, normalCreatureGroup, 0, new Random());
    assertEquals("Should be correct length", creatures.size(), number);
    assertTrue(
        "Should be correct type",
        creatures.stream().allMatch((Creature c) -> c.getType().equals(type)));
    assertNotEquals("Should be different creature objects", creatures.get(0), creatures.get(1));
  }
  
  @Test
  public void createCreatureGroupFixedTest() {
    final int x = 4, y = 5;
    List<Creature> creatures = CreatureFactory.createAll(maze, normalCreatureGroup, x, y);
    assertTrue("Should be on correct position",
        creatures.stream().allMatch((Creature c) -> c.getX() == x && c.getY() == y));
  }
  
  @Test
  public void createCreatureGroupRandomYTest() {
    final int x = 4;
    List<Creature> creatures = CreatureFactory.createAll(maze, normalCreatureGroup,x, new Random());
    double exampleY = creatures.get(0).getY();

    assertTrue("Should be on correct x positions",
        creatures.stream().allMatch((Creature c) -> c.getX() == x));
    assertTrue("Should be on different y positions",
        creatures.stream().anyMatch((Creature c) -> c.getY() != exampleY));
  }
  
  @Test
  public void createCreatureGroupRandomXTest() {
    final int y=5;
    List<Creature> creatures = CreatureFactory.createAll(maze, normalCreatureGroup,new Random(), y);
    double exampleX = creatures.get(0).getX();

    assertTrue("Should be on correct y positions",
        creatures.stream().allMatch((Creature c) -> c.getY() == y));
    assertTrue("Should be on different x positions",
        creatures.stream().anyMatch((Creature c) -> c.getX() != exampleX));
  }
  
  @Test
  public void createCreatureGroupRandomXAndYTest() {
    List<Creature> creatures = CreatureFactory.createAll(maze, normalCreatureGroup, new Random(), new Random());
    double exampleX = creatures.get(0).getX();
    double exampleY = creatures.get(0).getY();

    assertTrue("Should be on different x positions",
        creatures.stream().anyMatch((Creature c) -> c.getX() != exampleX));
    assertTrue("Should be on different y positions",
            creatures.stream().anyMatch((Creature c) -> c.getY() != exampleY));
  }
}
