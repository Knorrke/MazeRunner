package application.model.player;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player implements PlayerModelInterface {

  private IntegerProperty money;
  private IntegerProperty lifes;

  public Player(int startMoney, int startLifes) {
    this(new SimpleIntegerProperty(startMoney), new SimpleIntegerProperty(startLifes));
  }

  public Player(IntegerProperty money, IntegerProperty lifes) {
    this.money = money;
    this.lifes = lifes;
  }

  /** @return the money */
  public int getMoney() {
    return money.get();
  }
  /** @return the money property */
  public IntegerProperty moneyProperty() {
    return money;
  }
  /** @return the lives */
  public int getLifes() {
    return lifes.get();
  }
  /** @return the lives property */
  public IntegerProperty lifesProperty() {
    return lifes;
  }

  /** Loose a life */
  public void looseLife() {
    lifes.set(lifes.get() - 1);
  }

  /** Gain a life */
  public void gainLife() {
    lifes.set(lifes.get() + 1);
  }

  /**
   * Spend money
   *
   * @param costs
   */
  public void spendMoney(int costs) {
    money.set(money.get() - costs);
  }

  /**
   * Earn money
   *
   * @param earnings
   */
  public void earnMoney(int earnings) {
    money.set(money.get() + earnings);
  }
}
