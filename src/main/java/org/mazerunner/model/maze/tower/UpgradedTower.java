package org.mazerunner.model.maze.tower;

public class UpgradedTower extends AbstractTower {
  private AbstractTower old;

  public UpgradedTower(AbstractTower old, TowerUpgrade upgrade) {
    super(
        upgrade.getFireRateUpgrader().apply(old.getFireRate()),
        upgrade.getDamageUpgrader().apply(old.getDamage()),
        upgrade.getCosts() + old.getCosts(),
        upgrade.getVisualRangeUpgrader().apply(old.getVisualRange()),
        old.getType(),
        old.getWall());
    this.old = old;
    this.getUpgrades().addAll(old.getUpgrades());
  }

  @Override
  public Runnable createShooter(AbstractTower shooting) {
    return old.createShooter(shooting);
  }

  @Override
  public int getLevel() {
    return 1 + old.getLevel();
  }
}
