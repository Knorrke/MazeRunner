package application.model.level;

import application.model.actions.Action;

public class CreatureWaveAction extends Action {

  private LevelModelInterface level;

  public CreatureWaveAction(LevelModelInterface level) {
    this(level, Level.WAVE_DURATION, 1);
  }

  public CreatureWaveAction(LevelModelInterface level, double waveDuration, double startDelay) {
    super(waveDuration);
    super.setCountdown(startDelay);
    this.level = level;
  }

  @Override
  protected void execute() {
    level.sendNextCreatureWave();
    resetCountdown();
  }
}
