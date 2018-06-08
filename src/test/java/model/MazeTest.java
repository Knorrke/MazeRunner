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
import application.model.player.PlayerUpdater;
import application.model.player.PlayerUpdaterInterface;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class MazeTest {
  MazeModelInterface maze;
  PlayerUpdaterInterface playerUpdater;
  PlayerModelInterface playerMock;
  int x, y;

  @Before
  public void setup() {
    playerMock = Mockito.mock(Player.class);
    Mockito.doReturn(true).when(playerMock).spendMoney(ArgumentMatchers.anyInt());
    playerUpdater = Mockito.spy(new PlayerUpdater(playerMock));
    maze = new Maze();
    maze.setPlayerUpdater(playerUpdater);
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
    maze.buildWall(x, y);
    assertEquals("Maze should have a wall now", 1, walls.size());
    Mockito.verify(playerUpdater, Mockito.times(1)).newWallBuilt(walls.get(0));
    Mockito.verify(playerMock, Mockito.times(1)).spendMoney(ArgumentMatchers.anyInt());
  }

  @Test
  public void sellShouldEarnMoneyTest() {
    ObservableList<Wall> walls = maze.getWalls();
    Wall wall = maze.buildWall(x, y);
    assertTrue("maze contains built wall", walls.contains(wall));
    maze.sell(wall);
    assertFalse("Maze shouldn't contain the wall after sell", walls.contains(wall));
    Mockito.verify(playerUpdater, Mockito.times(1)).soldWall(wall);
    Mockito.verify(playerMock, Mockito.times(1)).earnMoney(ArgumentMatchers.anyInt());
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
    Mockito.verify(playerUpdater, Mockito.times(1)).leavingCreature(creature);
    Mockito.verify(playerMock, Mockito.times(1)).looseLife();
  }
}
