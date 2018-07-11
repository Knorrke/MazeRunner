package model.maze;

import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.Creature;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.maze.Wall;
import org.mazerunner.model.player.Player;
import org.mazerunner.model.player.PlayerModelInterface;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class MazeTest {
  MazeModelInterface maze;
  PlayerModelInterface playerMock;
  int x, y;

  @Before
  public void setup() {
    playerMock = Mockito.mock(Player.class);
    Mockito.doReturn(true).when(playerMock).spendMoney(ArgumentMatchers.anyInt());
    maze = new Maze();
    maze.setPlayerModel(playerMock);
    x = 2;
    y = 3;
  }

  @Test
  public void hasWallTest() {
    assertFalse("Shouldn't have a wall yet", maze.hasWallOn(x, y));
    maze.buildWall(x, y);
    assertTrue("Should have wall", maze.hasWallOn(x, y));
  }

  @Test
  public void getWallOnTest() {
    assertNull("Shouldn't have a wall yet", maze.getWallOn(x, y));
    maze.buildWall(x, y);
    assertNotNull("Should have a wall", maze.getWallOn(x, y));
    assertNull("Shouldn't have a wall somewhere else", maze.getWallOn(x + 1, y));
  }

  @Test
  public void hasCreatureTest() {
    assertFalse("Shouldn't have a creature yet", maze.hasCreatureNear(x, y));
    maze.addCreature(CreatureFactory.create(maze, CreatureType.NORMAL, x, y));
    assertTrue("Should have a creature now", maze.hasCreatureNear(x, y));
    assertTrue(
        "Should also work with a position a little beside the creature",
        maze.hasCreatureNear(x + 0.1, y + 0.1));
    assertFalse("Should not work everywhere", maze.hasCreatureNear(x + 1, y));
  }

  @Test
  public void getCreatureTest() {
    assertNull("Shouldn't have a creature yet", maze.getCreatureNear(x, y));
    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, x, y);
    maze.addCreature(creature);
    assertNotNull("Should have a creature", maze.getCreatureNear(x, y));
    assertEquals("Should be correct object", creature, maze.getCreatureNear(x, y));
    assertEquals(
        "Should also work with a position a little beside the creature",
        creature,
        maze.getCreatureNear(x + 0.1, y + 0.1));
    assertNull("Should not work everywhere", maze.getCreatureNear(x + 1, y));
  }

  @Test
  public void buildTest() {
    ObservableList<Wall> walls = maze.getWalls();

    AtomicBoolean listenerCalled = new AtomicBoolean(false);
    walls.addListener(
        (ListChangeListener<Wall>)
            c -> {
              assertFalse(
                  "listener should not have been called before", listenerCalled.getAndSet(true));
              while (c.next()) {
                assertTrue("Element should be added", c.wasAdded());
                assertEquals("Should be one element added", 1, c.getAddedSize());
                assertEquals("Position should be as build", x, c.getAddedSubList().get(0).getX());
                assertEquals("Position should be as build", y, c.getAddedSubList().get(0).getY());
              }
            });

    assertEquals("Maze should be empty at start", 0, walls.size());
    maze.buildWall(x, y);
    assertEquals("Maze should have a wall now", 1, walls.size());
    assertTrue("Listener should have been called", listenerCalled.get());
  }

  @Test
  public void buildWallOntoWallTest() {
    ObservableList<Wall> walls = maze.getWalls();
    assertEquals("Maze should be empty at start", 0, walls.size());
    maze.buildWall(x, y);
    assertEquals("Maze should have a wall now", 1, walls.size());
    maze.buildWall(x, y);
    assertEquals("Maze should still only have one wall", 1, walls.size());
    maze.buildWall(x + 1, y + 1);
    assertEquals("Maze should have two walls now", 2, walls.size());
  }

  @Test
  public void buildShouldCostTest() {
    ObservableList<Wall> walls = maze.getWalls();
    maze.buildWall(x, y);
    assertEquals("Maze should have a wall now", 1, walls.size());
    Mockito.verify(playerMock, Mockito.times(1)).spendMoney(walls.get(0).getCosts());
  }

  @Test
  public void sellShouldEarnMoneyTest() {
    ObservableList<Wall> walls = maze.getWalls();
    Wall wall = maze.buildWall(x, y);
    assertTrue("maze contains built wall", walls.contains(wall));
    maze.sell(wall);
    assertFalse("Maze shouldn't contain the wall after sell", walls.contains(wall));
    Mockito.verify(playerMock, Mockito.times(1)).earnMoney(wall.getCosts());
  }

  @Test
  public void sellShouldNotWorkIfCreaturesKnowOfTheWall() {
    Wall wall = maze.buildWall(x, y);
    Creature creatureWithKnowledge = CreatureFactory.create(maze, CreatureType.NORMAL, x - 1, y);
    Creature creatureWithoutKnowledge =
        CreatureFactory.create(maze, CreatureType.NORMAL, x - 1, y - 2);
    maze.addCreature(creatureWithKnowledge);
    maze.addCreature(creatureWithoutKnowledge);
    creatureWithKnowledge.markWalls();
    assertTrue("Creature should know the wall", creatureWithKnowledge.getVisitedMap().isWall(x, y));
    assertTrue(
        "Other creature should have no information",
        creatureWithoutKnowledge.getVisitedMap().isUnknown(x, y));

    maze.sell(wall);
    assertTrue("Wall should still be there", maze.hasWallOn(x, y));
    maze.creatureDied(creatureWithKnowledge);
    maze.sell(wall);
    assertFalse("Wall should now be gone", maze.hasWallOn(x, y));
  }

  @Test
  public void creatureShouldBeRemovedAfterReachingGoal() {
    int maxX = maze.getMaxWallX();
    Creature creature = CreatureFactory.create(maze, CreatureType.NORMAL, maxX - 2, 0);
    maze.addCreature(creature);
    maze.update(1);
    assertArrayEquals(
        new double[] {maxX - 1, 0}, new double[] {creature.getX(), creature.getY()}, 0.01);
    assertFalse(maze.getCreatures().contains(creature));
  }

  @Test
  public void looseLifeIfCreatureReachesGoal() {
    Creature creature =
        CreatureFactory.create(maze, CreatureType.NORMAL, maze.getMaxWallX() - 1, 0);
    maze.addCreature(creature);
    maze.update(1);
    Mockito.verify(playerMock, Mockito.times(1)).looseLife();
  }
}
