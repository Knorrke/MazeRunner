package model;

import static org.junit.Assert.*;

import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.NoTower;

import org.junit.Test;

public class TowerTest {

  @Test
  public void setupTest() {
    AbstractTower abstractTower = new NoTower();
    assertNotNull("AbstractTower should not be null", abstractTower);
  }
}
