package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mazerunner.model.GameState.BUILDING;
import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.Game;
import org.mazerunner.model.GameState;
import org.mazerunner.model.creature.CreatureFactory;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.level.LevelModelInterface;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mazerunner.model.player.PlayerModelInterface;

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
    game.getLevel().getCreatureTimeline().clear();
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

  @Test
  public void noticeWhenPlayerLost() {
    PlayerModelInterface player = game.getPlayer();
    LevelModelInterface level = game.getLevel();
    level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
    level.sendNextCreatureWave();

    int lifes = player.getLifes();
    for (int i = 0; i < lifes; i++) {
      player.looseLife();
    }
    assertEquals("State should be GAMEOVER now", GameState.GAMEOVER, game.getState());
  }

  @Test
  public void noticeWhenPlayerWins() {
    LevelModelInterface level = game.getLevel();
    level.getCreatureTimeline().clear();
    level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
    level.sendNextCreatureWave();

    MazeModelInterface maze = game.getMaze();
    maze.getCreatures().clear();
    assertEquals(
        "Game should be over now", level.getCreatureTimeline().size(), level.getWaveNumber());
    assertEquals("State should be WON now", GameState.WON, game.getState());
  }

  @Test
  public void setStateRunningWhenStart() {
    game.update(1);
    assertEquals("State should be RUNNING now", GameState.RUNNING, game.getState());
  }
}
