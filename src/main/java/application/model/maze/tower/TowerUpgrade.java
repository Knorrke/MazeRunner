package application.model.maze.tower;

public class TowerUpgrade {
  private double upgradedFireRate;
  private int additionalDamage;
  private int costs;
  private double upgradedVisualRange;

  public TowerUpgrade(
      double upgradedFireRate, int additionalDamage, int costs, double upgradedVisualRange) {
    this.upgradedFireRate = upgradedFireRate;
    this.additionalDamage = additionalDamage;
    this.costs = costs;
    this.upgradedVisualRange = upgradedVisualRange;
  }

  public AbstractTower createDecoratedTower(AbstractTower abstractTower) {
    return new UpgradedTower(abstractTower, this);
  }

  /** @return the upgradedFireRate */
  public double getUpgradedFireRate() {
    return upgradedFireRate;
  }

  /** @return the additionalDamage */
  public int getAdditionalDamage() {
    return additionalDamage;
  }

  /** @return the costs */
  public int getCosts() {
    return costs;
  }

  /** @return the upgradedVisualRange */
  public double getUpgradedVisualRange() {
    return upgradedVisualRange;
  }
}
