package model.level;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import application.model.creature.CreatureGroup;
import application.model.creature.CreatureType;
import application.model.level.Level;
import application.model.level.LevelModelInterface;
import application.model.maze.Maze;
import application.model.maze.MazeModelInterface;
import application.model.maze.MazeUpdater;
import application.model.maze.MazeUpdaterInterface;
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
    MazeModelInterface maze = Mockito.mock(Maze.class);
    Mockito.when(maze.getMaxWallX()).thenReturn(20);
    Mockito.when(maze.getMaxWallY()).thenReturn(10);
    MazeUpdaterInterface mazeUpdater = Mockito.spy(new MazeUpdater(maze));
    LevelModelInterface level = new Level();
    level.setMazeUpdater(mazeUpdater);

    level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 10));
    level.sendNextCreatureWave();
    Mockito.verify(mazeUpdater).nextWave(ArgumentMatchers.argThat(list -> list.size() == 10));
  }
}
