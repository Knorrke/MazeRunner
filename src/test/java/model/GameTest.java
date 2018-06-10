package model;

import static application.model.GameState.BUILDING;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import application.model.Game;
import application.model.creature.CreatureFactory;
import application.model.creature.CreatureType;
import application.model.maze.MazeModelInterface;
import application.model.player.PlayerModelInterface;

public class GameTest {

  Game game;

  @Before
  public void setup() {
    game = new Game();
  }

  @Test
  public void startState() {
    assertEquals("State should be BUILDING at start", BUILDING, game.getState());
  }

  @Test
  public void mazeAndPlayerConnected() {
    PlayerModelInterface player = game.getPlayer();
    MazeModelInterface maze = game.getMaze();
    int money = player.getMoney();
    maze.buildWall(2, 3);
    assertTrue("Should have spent money", player.getMoney() < money);

    int lifes = player.getLifes();
    maze.addCreature(CreatureFactory.create(maze, CreatureType.NORMAL, maze.getMaxWallX() - 2, 0));
    maze.update(1);
    assertEquals("Creature should be removed", 0, maze.getCreatures().size());
    assertEquals("Player should have lost a life", lifes - 1, player.getLifes());
  }
}
