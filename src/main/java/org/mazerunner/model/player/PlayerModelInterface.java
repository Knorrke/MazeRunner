package org.mazerunner.model.player;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import javafx.beans.property.IntegerProperty;
import org.mazerunner.model.ModelInterface;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Player.class, name = "Player"),
})
public interface PlayerModelInterface extends ModelInterface {
  /** @return the money */
  public int getMoney();
  /** @return the money property */
  public IntegerProperty moneyProperty();
  /** @return the lives */
  public int getLifes();
  /** @return the lives property */
  public IntegerProperty lifesProperty();

  /** Loose a life */
  public void looseLife();

  /** Gain a life */
  public void gainLife();

  /**
   * Spend money, if player got enough. Returns false otherwise.
   *
   * @param costs
   * @return true, if player has enough money, false otherwise
   */
  public boolean spendMoney(int costs);

  /**
   * Earn money
   *
   * @param earnings
   */
  public void earnMoney(int earnings);
}
