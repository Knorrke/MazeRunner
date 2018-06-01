package model;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import application.model.creature.Creature;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.maze.Wall;
import application.model.player.Player;
import application.model.player.PlayerModelInterface;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class MazeTest {
  MazeModelInterface maze;
  int x, y;

  @Before
  public void setup() {
    maze = new Maze();
    x = 2;
    y = 3;
  }

  @Test
  public void testHasWall() {
    assertFalse("Shouldn't have a wall yet", maze.hasWallOn(x, y));
    maze.addWall(new Wall(x, y));
    assertTrue("Should have wall", maze.hasWallOn(x, y));
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
    PlayerModelInterface player = Mockito.mock(Player.class);
    maze.setPlayer(player);
    maze.buildWall(x, y);
    assertEquals("Maze should have a wall now", 1, walls.size());
    Mockito.verify(player, Mockito.times(1)).spendMoney(ArgumentMatchers.anyInt());
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
    PlayerModelInterface player = Mockito.mock(Player.class);
    maze.setPlayer(player);
    Creature creature =
        CreatureFactory.create(maze, CreatureType.NORMAL, maze.getMaxWallX() - 1, 0);
    maze.addCreature(creature);
    maze.update(1);
    Mockito.verify(player, Mockito.times(1)).looseLife();
  }
}
