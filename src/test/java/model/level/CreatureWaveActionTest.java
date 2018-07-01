package model.level;

import org.junit.Test;
import org.mockito.Mockito;
import application.model.level.CreatureWaveAction;
import application.model.level.Level;
import application.model.level.LevelModelInterface;

public class CreatureWaveActionTest {
  @Test
  public void actionTriggerTest() {
    LevelModelInterface level = Mockito.mock(Level.class);
    CreatureWaveAction action = new CreatureWaveAction(level);
    Mockito.verify(level, Mockito.never()).sendNextCreatureWave();
    action.act(1); // should trigger Action and reset Countdown to 20
    Mockito.verify(level, Mockito.times(1)).sendNextCreatureWave();
    action.act(1); // shouldn't trigger Action
    Mockito.verify(level, Mockito.times(1)).sendNextCreatureWave();
    action.act(20); // should trigger Action once more
    Mockito.verify(level, Mockito.times(2)).sendNextCreatureWave();
  }
}
