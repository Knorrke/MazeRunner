package model.level;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mazerunner.model.creature.CreatureGroup;
import org.mazerunner.model.creature.CreatureType;
import org.mazerunner.model.level.Level;
import org.mazerunner.model.level.LevelModelInterface;
import org.mazerunner.model.maze.Maze;
import org.mazerunner.model.maze.MazeModelInterface;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class LevelTest {

  private LevelModelInterface level;

  @Before
  public void setUp() {
    level = new Level();
  }

  @Test
  public void observableCreatureTimelineTest() {
    ObservableList<CreatureGroup> creatureTimeline = level.getCreatureTimeline();
    @SuppressWarnings("unchecked")
    ListChangeListener<CreatureGroup> listener = Mockito.mock(ListChangeListener.class);
    creatureTimeline.addListener(listener);
    level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 10));
    Mockito.verify(listener).onChanged(ArgumentMatchers.any());
  }

  @Test
  public void sendNextCreatureWaveTest() {
    level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 10));
    MazeModelInterface mazeMock = addMazeMock();
    level.sendNextCreatureWave();
    Mockito.verify(mazeMock).addAllCreatures(ArgumentMatchers.argThat(list -> list.size() == 10));
  }

  @Test
  public void calculateTime() {
    int numberOfWaves = 3;
    addWaves(numberOfWaves);
    assertEquals(
        "Should calculate the time correctly",
        numberOfWaves * Level.WAVE_DURATION,
        level.calculateGameDuration(),
        0.001);
  }

  @Test
  public void calculatePercentageOfPassedTime() {
    int numberOfWaves = 4;
    addWaves(numberOfWaves);
    addMazeMock();
    level.sendNextCreatureWave();
    level.sendNextCreatureWave();
    assertEquals(
        "Should calculate the passed time correctly",
        0.25,
        level.calculatePassedTimePercentage(),
        0.001);
  }

  /*
   * Helper methods
   */

  private void addWaves(int numberOfWaves) {
    for (int i = 0; i < numberOfWaves; i++) {
      level.addCreatureToTimeline(new CreatureGroup(CreatureType.NORMAL, 20));
    }
  }

  private MazeModelInterface addMazeMock() {
    MazeModelInterface mazeMock = Mockito.mock(Maze.class);
    Mockito.when(mazeMock.getMaxWallX()).thenReturn(20);
    Mockito.when(mazeMock.getMaxWallY()).thenReturn(10);
    Mockito.when(mazeMock.getCreatures()).thenReturn(FXCollections.observableArrayList());
    level.setMazeModel(mazeMock);
    return mazeMock;
  }
}
