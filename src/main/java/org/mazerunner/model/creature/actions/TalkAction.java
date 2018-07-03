package org.mazerunner.model.creature.actions;

import org.mazerunner.model.baseactions.Action;
import org.mazerunner.model.baseactions.CountdownAction;
import org.mazerunner.model.creature.Creature;

public class TalkAction extends CountdownAction {

  private Creature talking;
  private Action interrupted;

  public TalkAction(int timeToCommunicate, Creature talking) {
    super(timeToCommunicate / 1000.0);
    this.talking = talking;
    this.interrupted = talking.getAction();
  }

  @Override
  protected void onFinish() {
    talking.setAction(interrupted);
  }

  public void adjustTalkTime(int timeToCommunicate) {
    this.setCountdown(Math.max(getCountdown(), timeToCommunicate / 1000.0));
  }
}
