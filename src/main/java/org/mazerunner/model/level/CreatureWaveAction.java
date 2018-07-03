package org.mazerunner.model.level;

import org.mazerunner.model.baseactions.CountdownAction;

public class CreatureWaveAction extends CountdownAction {

  private LevelModelInterface level;

  public CreatureWaveAction(LevelModelInterface level) {
    this(level, Level.WAVE_DURATION, 1);
  }

  public CreatureWaveAction(LevelModelInterface level, double waveDuration, double startDelay) {
    super(waveDuration, startDelay);
    this.level = level;
  }

  @Override
  protected void onFinish() {
    level.sendNextCreatureWave();
    resetCountdown();
  }
}
