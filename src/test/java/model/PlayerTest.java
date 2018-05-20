package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import application.model.player.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class PlayerTest {

  private int startMoney, startLives;
  private Player player;

  @Before
  public void setUp() {
    startMoney = 50;
    startLives = 20;
    player = new Player(startMoney, startLives);
  }

  @Test
  public void createTest() {
    assertNotNull(player);
    assertEquals(startMoney, player.getMoney());
    assertEquals(startMoney, player.moneyProperty().get());
    assertEquals(startLives, player.getLifes());
    assertEquals(startLives, player.lifesProperty().get());
  }

  @Test
  public void changeLifesTest() {
    assertEquals(startLives, player.getLifes());
    player.looseLife();
    assertEquals("After loosing a life it should be one less", startLives - 1, player.getLifes());
    player.gainLife();
    assertEquals(
        "After gaining a life it should be back to startLives", startLives, player.getLifes());
  }

  @Test
  public void looseTest() {
    BooleanProperty lost = new SimpleBooleanProperty(false);
    lost.bind(player.lifesProperty().lessThanOrEqualTo(0));

    // loose startLifes - 1 lifes (so 1 life remains)
    for (int i = 0; i < startLives - 1; i++) {
      player.looseLife();
    }
    assertEquals(player.getLifes(), 1);
    assertFalse("Not lost yet", lost.get());

    player.looseLife();
    assertEquals(player.getLifes(), 0);
    assertTrue("Lost now", lost.get());
  }

  @Test
  public void changeMoneyTest() {
    int costs = 20;
    assertEquals(startMoney, player.getMoney());
    assertTrue(startMoney > costs);
    player.spendMoney(costs);
    assertEquals("Spending should change money correctly", startMoney - costs, player.getMoney());
    player.earnMoney(costs);
    assertEquals("Earning should change money correctly", startMoney, player.getMoney());
  }
}
