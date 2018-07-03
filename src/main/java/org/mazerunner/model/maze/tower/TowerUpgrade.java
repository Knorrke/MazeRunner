package org.mazerunner.model.maze.tower;

import java.util.function.Function;

public class TowerUpgrade {
  private Function<Double, Double> fireRateUpgrader;
  private Function<Integer, Integer> damageUpgrader;
  private Function<Double, Double> visualRangeUpgrader;
  private int costs;

  public TowerUpgrade(
      Function<Double, Double> fireRateUpgrader,
      Function<Integer, Integer> damageUpgrader,
      Function<Double, Double> visualRangeUpgrader,
      int costs) {
    this.fireRateUpgrader = fireRateUpgrader;
    this.damageUpgrader = damageUpgrader;
    this.visualRangeUpgrader = visualRangeUpgrader;
    this.costs = costs;
  }

  public AbstractTower createDecoratedTower(AbstractTower abstractTower) {
    return new UpgradedTower(abstractTower, this);
  }

  /** @return the fireRateUpgrader */
  public Function<Double, Double> getFireRateUpgrader() {
    return fireRateUpgrader;
  }

  /** @return the damageUpgrader */
  public Function<Integer, Integer> getDamageUpgrader() {
    return damageUpgrader;
  }

  /** @return the visualRangeUpgrader */
  public Function<Double, Double> getVisualRangeUpgrader() {
    return visualRangeUpgrader;
  }

  /** @return the costs */
  public int getCosts() {
    return costs;
  }
}
