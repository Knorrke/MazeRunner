package org.mazerunner.model.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player implements PlayerModelInterface {

  private IntegerProperty money;
  private IntegerProperty lifes;

  @JsonCreator
  public Player(@JsonProperty("money") int startMoney, @JsonProperty("lifes") int startLifes) {
    this(new SimpleIntegerProperty(startMoney), new SimpleIntegerProperty(startLifes));
  }

  public Player(IntegerProperty money, IntegerProperty lifes) {
    this.money = money;
    this.lifes = lifes;
  }

  /**
   * @return the money
   */
  @Override
  public int getMoney() {
    return money.get();
  }
  /**
   * @return the money property
   */
  @Override
  public IntegerProperty moneyProperty() {
    return money;
  }
  /**
   * @return the lives
   */
  @Override
  public int getLifes() {
    return lifes.get();
  }
  /**
   * @return the lives property
   */
  @Override
  public IntegerProperty lifesProperty() {
    return lifes;
  }

  @Override
  public void looseLife() {
    lifes.set(lifes.get() - 1);
  }

  @Override
  public void gainLife() {
    lifes.set(lifes.get() + 1);
  }

  @Override
  public boolean spendMoney(int costs) {
    if (money.get() >= costs) {
      money.set(money.get() - costs);
      return true;
    }

    return false;
  }

  @Override
  public void earnMoney(int earnings) {
    money.set(money.get() + earnings);
  }
}
