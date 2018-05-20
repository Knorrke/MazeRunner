package model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.NoTower;

public class TowerTest {

  @Test
  public void setupTest() {
    AbstractTower abstractTower = new NoTower();
    assertNotNull("AbstractTower should not be null", abstractTower);
  }
}
