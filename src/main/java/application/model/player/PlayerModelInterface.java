package application.model.player;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import application.model.ModelInterface;
import javafx.beans.property.IntegerProperty;
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
   * Spend money
   *
   * @param costs
   */
  public void spendMoney(int costs);

  /**
   * Earn money
   *
   * @param earnings
   */
  public void earnMoney(int earnings);
}
