package view;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

public class CreatureViewTest extends AbstractViewTest {
	@Test
	public void checkCreatureVisible() {
		game.start();
		assertEquals("There should be a creature", false, maze.getCreatures().isEmpty());
		verifyThat("#maze", NodeMatchers.hasChild(".creature"), collectInfos());
		game.pause();
	}
}
