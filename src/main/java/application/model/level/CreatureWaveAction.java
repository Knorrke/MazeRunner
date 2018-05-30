package application.model.level;

import application.model.actions.Action;

public class CreatureWaveAction extends Action {

  private LevelModelInterface level;

  public CreatureWaveAction(LevelModelInterface level) {
    super(20);
    super.setCountdown(1);
    this.level = level;
  }

  @Override
  protected void execute() {
    level.sendNextCreatureWave();
    super.resetCountdown();
  }
}
