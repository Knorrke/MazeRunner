package model.creature;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import application.model.baseactions.Action;
import application.model.creature.Creature;
import application.model.creature.actions.TalkAction;

public class TalkActionTest {

  private Creature creatureMock;
  private Action previous;

  @Before
  public void setUp() {
    creatureMock = Mockito.mock(Creature.class);
    previous = Mockito.mock(Action.class);
    Mockito.when(creatureMock.getAction()).thenReturn(previous);
  }

  @Test
  public void resetAction() {
    Mockito.verifyNoMoreInteractions(previous);
    Action talkAction = new TalkAction(500, creatureMock);
    Mockito.verify(creatureMock).getAction();
    talkAction.act(500);
    Mockito.verify(creatureMock).setAction(previous);
  }

  @Test
  public void adjustTimeTest() {
    TalkAction talkAction = new TalkAction(500, creatureMock);
    int timeInMillis = 700;
    talkAction.adjustTalkTime(timeInMillis);
    assertEquals(timeInMillis / 1000.0, talkAction.getCountdown(), 0.0001);
  }
}
