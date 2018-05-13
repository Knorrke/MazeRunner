package application.model.maze.tower;

import application.model.gameloop.ActorInterface;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public abstract class AbstractTower implements ActorInterface {

  protected DoubleProperty fireRate;
  protected IntegerProperty damage;
  protected IntegerProperty costs;
  protected DoubleProperty visualRange;

  public void shoot() {
    // TODO
  }

  /** @return the fireRate value */
  public double getFireRate() {
    return fireRate.get();
  }

  /** @param fireRate the fireRate to set */
  public void setFireRate(double fireRate) {
    this.fireRate.set(fireRate);
  }

  /** @return the fireRate property */
  public DoubleProperty fireRateProperty() {
    return fireRate;
  }

  /** @return the damage value */
  public int getDamage() {
    return damage.get();
  }

  /** @param damage the damage to set */
  public void setDamage(int damage) {
    this.damage.set(damage);
  }

  /** @return the damage property */
  public IntegerProperty damageProperty() {
    return damage;
  }

  /** @return the costs value */
  public int getCosts() {
    return costs.get();
  }

  /** @param costs the costs to set */
  public void setCosts(int costs) {
    this.costs.set(costs);
  }

  /** @return the costs property */
  public IntegerProperty costsProperty() {
    return costs;
  }

  /** @return the visualRange value */
  public double getVisualRange() {
    return visualRange.get();
  }

  /** @param visualRange the visualRange to set */
  public void setVisualRange(double visualRange) {
    this.visualRange.set(visualRange);
  }

  /** @return the visualRange property */
  public DoubleProperty visualRangeProperty() {
    return visualRange;
  }
}
