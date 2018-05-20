package application.model.maze.tower;

public class NoTower extends AbstractTower {
  
  public NoTower() {
    setFireRate(0);
    setDamage(0);
    setCosts(0);
    setVisualRange(0);
  }
  @Override
  public void act(double dt) {}
}
