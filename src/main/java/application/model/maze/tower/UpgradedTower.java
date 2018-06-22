package application.model.maze.tower;

public class UpgradedTower extends AbstractTower {
  private AbstractTower decorated;

  public UpgradedTower(AbstractTower decorated, TowerUpgrade upgrade) {
    super(
        upgrade.getUpgradedFireRate(),
        upgrade.getAdditionalDamage(),
        upgrade.getCosts(),
        upgrade.getUpgradedVisualRange(),
        decorated.getType(),
        decorated.getWall());
    this.decorated = decorated;
    decorated.setWall(getWall());
    this.getUpgrades().addAll(decorated.getUpgrades());
  }

  @Override
  public Runnable createShooter(AbstractTower shooting) {
    return decorated.createShooter(shooting);
  }

  @Override
  public int getCosts() {
    return super.getCosts() + decorated.getCosts();
  }

  @Override
  public int getDamage() {
    return super.getDamage() + decorated.getDamage();
  }

  @Override
  public int getLevel() {
    return 1 + decorated.getLevel();
  }
}
