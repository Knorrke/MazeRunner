package view;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import org.junit.Test;

public class PlayerViewTest extends AbstractViewTest {

  @Test
  public void labelsShouldDisplayCorrectInformation() {
    int lifes = player.getLifes();
    verifyThat("#lifes", hasText(Integer.toString(lifes)));

    int money = player.getMoney();
    verifyThat("#money", hasText(Integer.toString(money)));
  }

  @Test
  public void labelsShouldChangeWhenModelChanges() {
    int lifes = player.getLifes();
    int money = player.getMoney();

    interact(
        () -> {
          player.looseLife();
        });
    verifyThat("#lifes", hasText(Integer.toString(lifes - 1)));

    int earnings = 50;
    interact(
        () -> {
          player.earnMoney(earnings);
        });
    verifyThat("#money", hasText(Integer.toString(money + earnings)));
  }
}
