package modell;

import static org.junit.Assert.*;

import org.junit.Test;

import model.tower.NoTower;
import model.tower.Tower;

public class TowerTest {

	@Test
	public void setupTest() {
		Tower tower = new NoTower();
		assertNotNull("Tower should not be null", tower);
	}
}
