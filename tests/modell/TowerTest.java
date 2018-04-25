package modell;

import static org.junit.Assert.*;

import org.junit.Test;

import model.maze.tower.AbstractTower;
import model.maze.tower.NoTower;

public class TowerTest {

	@Test
	public void setupTest() {
		AbstractTower abstractTower = new NoTower();
		assertNotNull("AbstractTower should not be null", abstractTower);
	}
}
