package model.level;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import application.model.Game;
import application.model.GameModelInterface;
import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import application.model.level.Level;
import application.model.level.LevelModelInterface;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class LevelTest {

  @Test
  public void observableCreatureTimelineTest() {
    LevelModelInterface level = new Level();
    ObservableList<CreatureGroup> creatureTimeline = level.getCreatureTimeline();
    @SuppressWarnings("unchecked")
    ListChangeListener<CreatureGroup> listener = Mockito.mock(ListChangeListener.class);
    creatureTimeline.addListener(listener);
    level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 10));
    Mockito.verify(listener).onChanged(ArgumentMatchers.any());
  }

  @Test
  public void sendNextCreatureWaveTest() {
    GameModelInterface game = Mockito.mock(Game.class);
    MazeModelInterface maze = Mockito.mock(Maze.class);
    LevelModelInterface level = new Level(game);

    Mockito.when(game.getMaze()).thenReturn(maze);
    Mockito.when(maze.getMaxWallX()).thenReturn(20);
    Mockito.when(maze.getMaxWallY()).thenReturn(10);

    level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 10));
    level.sendNextCreatureWave();
    Mockito.verify(game).nextWave(ArgumentMatchers.argThat(list -> list.size() == 10));
  }
}
